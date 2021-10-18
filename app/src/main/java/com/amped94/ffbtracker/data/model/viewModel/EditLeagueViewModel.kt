package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class EditLeagueViewModel(val leagueId: Long) : ViewModel() {
    val leagueAndPlayers = mutableStateOf<LeagueAndPlayers?>(null)

    init {
        getLeague()
    }

    fun getLeague() {
        viewModelScope.launch {
            leagueAndPlayers.value = SleeperRepository.getLeagueAndPlayers(leagueId)
        }
    }
}