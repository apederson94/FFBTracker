package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val playersAndLeagues = mutableStateListOf<PlayerAndLeagues>()
    val playersAndLeaguesToShow = mutableStateListOf<PlayerAndLeagues>()
    val isLoading = mutableStateOf(false)

    var onFABTapped = mutableStateOf({})
    var title = mutableStateOf("FFBTracker")

    init {
        viewModelScope.launch {
            getPlayersAndLeagues()
        }
    }

    fun searchPlayers(text: String) {
        playersAndLeaguesToShow.clear()
        if (text.isNotBlank()) {
            playersAndLeaguesToShow.addAll(
                playersAndLeagues
                    .filter { "${it.player.firstName} ${it.player.lastName}".lowercase().contains(text.lowercase()) }
            )
        } else {
            playersAndLeaguesToShow.addAll(playersAndLeagues)
        }
    }
    
    private fun getPlayersAndLeagues() {
        isLoading.value = true
        viewModelScope.launch {
            val newPlayersAndLeagues = SleeperRepository.getPlayersAndLeagues()

            playersAndLeagues.clear()
            playersAndLeagues.addAll(newPlayersAndLeagues)
            playersAndLeaguesToShow.addAll(newPlayersAndLeagues)
            isLoading.value = false
        }
    }
}