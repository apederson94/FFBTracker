package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class AddPlayersToLeagueViewModel : ViewModel() {
    private val _league = MutableLiveData<League>()
    val league: LiveData<League> = _league

    private val _suggestions = MutableLiveData<List<Player>>()
    val suggestions: LiveData<List<Player>> = _suggestions

    val selectedPlayers = mutableStateMapOf<String, Player>()

    init {
        viewModelScope.launch {
            val latestLeague = SleeperRepository.getLatestLeague()
            _league.postValue(latestLeague)
        }
    }

    fun getAutofillSuggestions(position: Position, searchText: String) {
        viewModelScope.launch {
            val suggestions =
                if (
                    position != Position.Bench
                    && !position.isSuperFLEX()
                ) {
                    val searchPosition = if (position == Position.DST) "DEF" else position.title
                    SleeperRepository.db.playerDao()
                        .searchPlayersByPosition(searchPosition, searchText)
                } else {
                    SleeperRepository.db.playerDao().searchPlayers(searchText)
                }.filter { player ->
                    selectedPlayers.values.none { it.playerId == player.playerId }
                }
            _suggestions.postValue(suggestions)
        }
    }

    fun clearSuggestions() {
        _suggestions.postValue(emptyList())
    }

    fun savePlayersToLeague() {
        viewModelScope.launch {
            league.value?.let { league ->
                val crossRefs = selectedPlayers.values.map {
                    PlayerLeagueCrossRef(
                        leagueId = league.leagueId,
                        playerId = it.playerId
                    )
                }
                SleeperRepository.savePlayersToLeague(crossRefs)
            }
        }
    }
}