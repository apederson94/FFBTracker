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

class CreateLeagueViewModel : ViewModel() {
    val selectedPlayers = mutableStateListOf<SelectedPlayer>()
    val addPlayerFields = mutableStateListOf(PlayerSelectionFieldModel())
    val leagueName = mutableStateOf(TextFieldValue(""))

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
            selectedPlayers.add(selectedPlayer)
        }
    }

    fun saveLeague() {
        var numQB = 0
        var numRB = 0
        var numWR = 0
        var numTE = 0
        var numFLEX = 0
        var numK = 0
        var numDST = 0
        var numSuperFLEX = 0
        var numBench = 0

        selectedPlayers.forEach {
            when (it.position) {
                Position.QB -> numQB++
                Position.RB -> numRB++
                Position.WR -> numWR++
                Position.TE -> numTE++
                Position.FLEX -> numFLEX++
                Position.K -> numK++
                Position.DST -> numDST++
                Position.SuperFLEX -> numSuperFLEX++
                Position.Bench -> numBench++
            }
        }

        val newLeague = League(
            leagueId = 0,
            externalLeagueId = "",
            associatedUserId = -1,
            name = leagueName.value.text,
            type = FantasyProvider.Custom,
            numQB =  numQB,
            numRB = numRB,
            numWR = numWR,
            numTE = numTE,
            numFLEX = numFLEX,
            numK = numK,
            numDST = numDST,
            numSuperFLEX = numSuperFLEX,
            numBench = numBench
        )

        viewModelScope.launch {
            SleeperRepository.saveLeagueAndPlayers(newLeague, selectedPlayers.map{ it.player })
        }
    }
}