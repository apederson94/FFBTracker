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
    val allTeams = mutableStateListOf<String>()
    val selectedTeam: MutableState<String?> = mutableStateOf(null)

    val isLoading = mutableStateOf(false)
    val searchText = mutableStateOf("")
    val selectedPosition: MutableState<Position?> = mutableStateOf(null)

    var onFABTapped = mutableStateOf({})
    var title = mutableStateOf("FFBTracker")

    init {
        viewModelScope.launch {
            loadLeagueData()
        }
    }

    fun filterPlayers(text: String, position: Position?, team: String?) {
        val filteredPlayersAndLeages = playersAndLeagues
            .filter {
                if (text.isNotBlank()) {
                    "${it.player.firstName} ${it.player.lastName}".lowercase()
                        .contains(text.lowercase())
                } else true
            }
            .filter {
                val positionFilter = position == null || it.player.position == position.title
                val teamFilter = team == null || it.player.team == team

                positionFilter && teamFilter
            }

        playersAndLeaguesToShow.clear()
        playersAndLeaguesToShow.addAll(filteredPlayersAndLeages)
    }

    fun filterPlayers(position: Position?) {
        selectedPosition.value = position
        filterPlayers(searchText.value, position, selectedTeam.value)
    }

    fun filterPlayers(team: String?) {
        selectedTeam.value = team
        filterPlayers(searchText.value, selectedPosition.value, team)
    }

    fun searchPlayers(text: String) {
        searchText.value = text
        filterPlayers(text, selectedPosition.value, selectedTeam.value)
    }

    private fun loadLeagueData() {
        isLoading.value = true
        viewModelScope.launch {
            getPlayersAndLeagues()
            getTeams()
            isLoading.value = false
        }

    }

    private suspend fun getPlayersAndLeagues() {
        val newPlayersAndLeagues = SleeperRepository.getPlayersAndLeagues()

        playersAndLeagues.clear()
        playersAndLeagues.addAll(newPlayersAndLeagues)
        playersAndLeaguesToShow.addAll(newPlayersAndLeagues)
    }

    private suspend fun getTeams() {
        val teams = SleeperRepository.getTeams()

        allTeams.clear()
        allTeams.addAll(teams)
    }
}