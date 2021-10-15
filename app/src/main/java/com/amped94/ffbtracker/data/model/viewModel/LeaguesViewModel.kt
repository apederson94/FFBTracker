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

sealed class Position(val title: String) {

    object SuperFLEX : Position(title = "SuperFLEX") {
        object QB : Position(title = "QB")
        object FLEX : Position(title = "FLEX") {
            object RB : Position(title = "RB")
            object WR : Position(title = "WR")
            object TE : Position(title = "TE")
        }
    }

    object K : Position(title = "K")
    object DEF : Position(title = "D/ST")
}