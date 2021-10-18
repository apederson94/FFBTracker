package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {
    var showDeleteAlert = mutableStateOf(false)
    var leagueToDelete = mutableStateOf<League?>(null)

    private val _leaguesAndPlayers: MutableLiveData<List<LeagueAndPlayers>> = MutableLiveData()
    val leaguesAndPlayers: LiveData<List<LeagueAndPlayers>> = _leaguesAndPlayers

    init {
        getLeagues()
    }

    fun getLeagues() {
        viewModelScope.launch {
            val queriedResults = SleeperRepository.getLeaguesAndPlayers()
            _leaguesAndPlayers.postValue(queriedResults)
        }
    }

    fun deleteLeague() {
        showDeleteAlert.value = false
        leagueToDelete.value?.let {
            viewModelScope.launch {
                SleeperRepository.deleteLeague(it)
                getLeagues()
            }
        }
    }

    fun showDeleteAlert(league: League) {
        leagueToDelete.value = league
        showDeleteAlert.value = true
    }

    fun dismissDeleteAlert() {
        leagueToDelete.value = null
        showDeleteAlert.value = false
    }
}

enum class Position(val title: String) {
    QB("QB"),
    RB("RB"),
    WR("WR"),
    TE("TE"),
    K("K"),
    DST("DST");

    fun isFLEX(): Boolean {
        return this == RB || this == WR || this == TE
    }

    fun isSuperFLEX(): Boolean {
        return this == QB || isFLEX()
    }

    companion object {
        fun getFromString(value: String): Position {
            return when (value) {
                "QB" -> QB
                "RB" -> RB
                "WR" -> WR
                "TE" -> TE
                "K" -> K
                "DEF" -> DST
                else -> throw Error("$value does not map to a position in Position enum")
            }
        }
    }
}