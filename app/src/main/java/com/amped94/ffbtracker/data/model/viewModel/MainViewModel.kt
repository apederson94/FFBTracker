package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val playersAndLeagues = mutableStateListOf<PlayerAndLeagues>()
    val playersAndLeaguesToShow = mutableStateListOf<PlayerAndLeagues>()
    val isLoading = mutableStateOf(false)
    val searchText = mutableStateOf("")
    val selectedPosition: MutableState<Position?> = mutableStateOf(null)

    var onFABTapped = mutableStateOf({})
    var title = mutableStateOf("FFBTracker")

    init {
        viewModelScope.launch {
            getPlayersAndLeagues()
        }
    }

    fun filterPlayers(text: String, position: Position?) {
        val filteredPlayersAndLeages = playersAndLeagues
            .filter {
                if (text.isNotBlank()) {
                    "${it.player.firstName} ${it.player.lastName}".lowercase()
                        .contains(text.lowercase())
                } else true
            }
            .filter {
                if (position != null) {
                    it.player.position == position.title
                } else true
            }

        playersAndLeaguesToShow.clear()
        playersAndLeaguesToShow.addAll(filteredPlayersAndLeages)
    }

    fun filterPlayers(position: Position?) {
        selectedPosition.value = position
        filterPlayers(searchText.value, position)
    }

    fun searchPlayers(text: String) {
        searchText.value = text
        filterPlayers(text, selectedPosition.value)
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