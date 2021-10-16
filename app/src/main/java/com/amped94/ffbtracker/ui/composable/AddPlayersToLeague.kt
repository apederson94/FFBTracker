package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.db.entity.Player
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.AddPlayersToLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.PlayerFieldViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun AddPlayersToLeague(navController: NavController) {
    val viewModel by remember { mutableStateOf(AddPlayersToLeagueViewModel()) }
    val league by viewModel.league.observeAsState()
    val suggestions by viewModel.suggestions.observeAsState()
    val fields = hashMapOf<Position, Int>().apply {
        league?.let { league ->
            put(Position.SuperFLEX.QB, league.numQB)
            put(Position.SuperFLEX.FLEX.RB, league.numRB)
            put(Position.SuperFLEX.FLEX.WR, league.numWR)
            put(Position.SuperFLEX.FLEX.TE, league.numTE)
            put(Position.SuperFLEX.FLEX, league.numFLEX)
            put(Position.K, league.numK)
            put(Position.DST, league.numDST)
            put(Position.SuperFLEX, league.numSuperFLEX)
            put(Position.Bench, league.numBench)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val sortedFields = fields.toList().sortedBy {
            when (it.first) {
                Position.SuperFLEX.QB -> 0
                Position.SuperFLEX.FLEX.RB -> 1
                Position.SuperFLEX.FLEX.WR -> 2
                Position.SuperFLEX.FLEX.TE -> 3
                Position.SuperFLEX.FLEX -> 4
                Position.K -> 5
                Position.DST -> 6
                Position.SuperFLEX -> 7
                Position.Bench -> 8
            }
        }

        sortedFields.forEach { (position, numPlayers) ->
            items(numPlayers) { index ->
                PlayerField(
                    type = position,
                    suggestions = suggestions,
                    selectedPlayer = viewModel.selectedPlayers["${position.title}$index"],
                    getSuggestions = {
                        viewModel.getAutofillSuggestions(position, it)
                    },
                ) { player ->
                    viewModel.selectedPlayers["${position.title}$index"] = player
                    viewModel.clearSuggestions()
                }
            }
        }

        item {
            Button(onClick = {
                viewModel.savePlayersToLeague()
                navController.popBackStack(
                    Screen.Leagues.route,
                    inclusive = false
                )
            }, modifier = Modifier.padding(vertical = 8.dp)) {
                Text("Save & Finish")
            }
        }
    }


}

@Composable
fun PlayerField(
    type: Position,
    suggestions: List<Player>?,
    selectedPlayer: Player?,
    getSuggestions: (String) -> Unit,
    onPlayerSelected: (Player) -> Unit
) {
    var text by remember {
        mutableStateOf(TextFieldValue(selectedPlayer?.let { "${it.firstName} ${it.lastName}" }
            ?: ""))
    }
    var showSuggestions by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.text.length > 2) {
                    getSuggestions(it.text)
                }
                text = it
            },
            label = {
                Text(type.title)
            },
            modifier = Modifier.onFocusChanged {
                showSuggestions = it.isFocused
            }
        )

        if (showSuggestions) {
            suggestions?.forEach {
                Text(
                    "${it.firstName} ${it.lastName}",
                    modifier = Modifier
                        .clickable {
                            text = TextFieldValue("${it.firstName} ${it.lastName}")
                            onPlayerSelected(it)
                        }
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun PlayerField(type: Position, selectedPlayer: Player?, onPlayerSelected: (Player) -> Unit) {
    val viewModel by remember {
        mutableStateOf(PlayerFieldViewModel().apply {
            selectedPlayer?.let {
                selectPlayer(
                    it
                )
            }
        })
    }
    val suggestions by viewModel.autofillSuggestions.observeAsState()
    val text by viewModel.text.observeAsState()

    Column {
        OutlinedTextField(
            value = text ?: "",
            onValueChange = {
                if (it.length > 2) {
                    viewModel.getAutofillSuggestions(type, it)
                }
                viewModel.text.postValue(it)
            },
            label = {
                Text(type.title)
            }
        )
        suggestions?.forEach {
            Text(
                "${it.firstName} ${it.lastName}",
                modifier = Modifier
                    .clickable {
                        viewModel.selectPlayer(it)
                        onPlayerSelected(it)
                    }
                    .padding(8.dp)
            )
        }
    }
}