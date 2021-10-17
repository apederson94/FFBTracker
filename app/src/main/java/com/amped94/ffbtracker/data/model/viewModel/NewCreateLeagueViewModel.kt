package com.amped94.ffbtracker.data.model.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.repository.SleeperRepository
import kotlinx.coroutines.launch
import java.util.*

data class SelectedPlayer(
    val player: Player,
    var position: Position
)

data class PlayerSelectionField(
    var player: Player? = null,
    var position: Position = Position.QB,
    var textFieldValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue()),
    val id: UUID = UUID.randomUUID(),
    var suggestions: List<Player> = emptyList()
)

class NewCreateLeagueViewModel : ViewModel() {
    val selectedPlayers = mutableStateListOf<SelectedPlayer>()
    val addPlayerFields = mutableStateListOf<PlayerSelectionField>()

    fun getSugestions(selectionField: PlayerSelectionField) {
        viewModelScope.launch {
            val suggestions = SleeperRepository.searchPlayersByPositionAndName(
                selectionField.position,
                selectionField.textFieldValue.value.text
            ).filter { player ->
                selectedPlayers.none { it.player.playerId == player.playerId }
            }
            selectionField.suggestions = suggestions
        }
    }
}