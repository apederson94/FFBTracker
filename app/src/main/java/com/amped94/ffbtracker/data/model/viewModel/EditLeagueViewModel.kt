package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.model.db.entity.PlayerLeagueCrossRef
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch

class EditLeagueViewModel(val leagueId: Long) : EditableLeagueViewModel() {
    val leagueAndPlayers = mutableStateOf<LeagueAndPlayers?>(null)

    init {
        playerSelectionFieldModels.clear()
        getLeague()
    }

    fun getLeague() {
        viewModelScope.launch {
            val queriedLeagueAndPlayers = SleeperRepository.getLeagueAndPlayers(leagueId)
            leagueAndPlayers.value = queriedLeagueAndPlayers
            leagueName.value = TextFieldValue(queriedLeagueAndPlayers.league.name)

            val savedSelectedPlayers = mutableListOf<SelectedPlayer>()
            queriedLeagueAndPlayers.players.forEach {
                savedSelectedPlayers.add(
                    SelectedPlayer(
                        player = it,
                        position = Position.getFromString(it.position)
                    )
                )
            }
            selectedPlayers.addAll(savedSelectedPlayers)
        }
    }

    override fun saveLeague() {
        leagueAndPlayers.value?.league?.let {
            val updatedLeague = League(
                leagueId = it.leagueId,
                externalLeagueId = it.externalLeagueId,
                associatedUserId = it.associatedUserId,
                name = leagueName.value.text,
                type = it.type
            )
            val updatedCrossRefs = selectedPlayers.map { item ->
                PlayerLeagueCrossRef(
                    leagueId = it.leagueId,
                    playerId = item.player.playerId
                )
            }
            viewModelScope.launch {
                SleeperRepository.updateLeague(updatedLeague)
                SleeperRepository.updatePlayerLeagueCrossRefs(
                    updatedLeague.leagueId,
                    updatedCrossRefs
                )
            }
            onSaveFinished()
        }
    }
}