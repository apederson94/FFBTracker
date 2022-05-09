package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val playersAndLeagues = mutableStateListOf<PlayerAndLeagues>()

    var onFABTapped = mutableStateOf({})
    var title = mutableStateOf("FFBTracker")

    init {
        viewModelScope.launch {
            getPlayersAndLeaguesInitial()
        }
    }

    private fun getPlayersAndLeaguesInitial() {
        viewModelScope.launch {
            val newPlayersAndLeagues = SleeperRepository.getPlayersAndLeagues()

            playersAndLeagues.clear()
            playersAndLeagues.addAll(newPlayersAndLeagues)
        }
    }

    fun getPlayersAndLeagues() {
        viewModelScope.launch {
            val newPlayersAndleagues = SleeperRepository.getPlayersAndLeagues()

            playersAndLeagues.clear()
            playersAndLeagues.addAll(newPlayersAndleagues)
        }
    }
}