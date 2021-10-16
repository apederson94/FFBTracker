package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class AddPlayersToLeagueViewModel: ViewModel() {
    private val _league = MutableLiveData<League>()
    val league: LiveData<League> = _league

    init {
        viewModelScope.launch {
            val latestLeague = SleeperRepository.getLatestLeague()
            _league.postValue(latestLeague)
        }
    }

    fun savePlayersToLeague(players: List<Player>) {
        viewModelScope.launch {
            league.value?.let { league ->
                val crossRefs = players.map {
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