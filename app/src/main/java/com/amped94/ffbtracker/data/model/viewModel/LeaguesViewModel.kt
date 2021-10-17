package com.amped94.ffbtracker.data.model.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class LeaguesViewModel : ViewModel() {
    val leagueName: MutableLiveData<String> = MutableLiveData("")

    private val _leaguesAndPlayers: MutableLiveData<List<LeagueAndPlayers>> = MutableLiveData()
    val leaguesAndPlayers: LiveData<List<LeagueAndPlayers>> = _leaguesAndPlayers

    init {
        viewModelScope.launch {
            val queriedResults = SleeperRepository.getLeaguesAndPlayers()
            _leaguesAndPlayers.postValue(queriedResults)
        }
    }
}

enum class Position(val title: String) {
    QB("QB"),
    RB("RB"),
    WR("WR"),
    TE("TE"),
    FLEX("FLEX"),
    K("K"),
    DST("DST"),
    SuperFLEX("SF"),
    Bench("BN");

    fun isFLEX(): Boolean {
        return this == RB || this == WR || this == TE
    }

    fun isSuperFLEX(): Boolean {
        return this == QB || isFLEX()
    }
}