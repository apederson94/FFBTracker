package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch
import java.util.*

data class SelectedPlayer(
    val player: Player,
    var position: Position,
    val id: UUID = UUID.randomUUID()
)

data class PlayerSelectionFieldModel(
    var player: Player? = null,
    var position: Position = Position.QB,
    var textFieldValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val id: UUID = UUID.randomUUID(),
    var suggestions: SnapshotStateList<Player> = mutableStateListOf()
)

open class EditableLeagueViewModel : ViewModel() {
    val selectedPlayers = mutableStateListOf<SelectedPlayer>()
    val playerSelectionFieldModels = mutableStateListOf(PlayerSelectionFieldModel())
    val leagueName = mutableStateOf(TextFieldValue(""))
    var onSaveFinished: () -> Unit = {}


    fun getSuggestions(selectionFieldModel: PlayerSelectionFieldModel) {
        viewModelScope.launch {
            val suggestions = SleeperRepository.searchPlayersByPositionAndName(
                selectionFieldModel.position,
                selectionFieldModel.textFieldValue.value.text
            ).filter { player ->
                selectedPlayers.none { it.player.playerId == player.playerId }
            }
            selectionFieldModel.suggestions.clear()
            selectionFieldModel.suggestions.addAll(suggestions)
        }
    }

    fun addNewlySelectedPlayer(selectionFieldModel: PlayerSelectionFieldModel) {
        selectionFieldModel.player?.let {
            val selectedPlayer = SelectedPlayer(
                player = it,
                position = selectionFieldModel.position
            )
            playerSelectionFieldModels.remove(selectionFieldModel)
            selectedPlayers.add(selectedPlayer)
        }
    }

    open fun saveLeague() {
        val newLeague = League(
            leagueId = 0,
            externalLeagueId = "",
            associatedUserId = -1,
            name = leagueName.value.text,
            type = FantasyProvider.Custom
        )

        viewModelScope.launch {
            SleeperRepository.saveLeagueAndPlayers(newLeague, selectedPlayers.map { it.player })
            onSaveFinished()
        }
    }

    fun removeSelectedPlayer(selectedPlayer: SelectedPlayer) {
        selectedPlayers.remove(selectedPlayer)
    }

    fun removePlayerSelectionField(model: PlayerSelectionFieldModel) {
        playerSelectionFieldModels.remove(model)
    }

    fun onFABTapped() {
        playerSelectionFieldModels.add(PlayerSelectionFieldModel())
    }
}