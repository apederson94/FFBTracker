package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.amped94.ffbtracker.data.model.viewModel.NewCreateLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.PlayerSelectionField
import com.amped94.ffbtracker.data.model.viewModel.Position
import com.amped94.ffbtracker.data.model.viewModel.SelectedPlayer

@Composable
fun NewCreateLeague() {
    val viewModel by remember { mutableStateOf(NewCreateLeagueViewModel()) }
    var leagueName by remember { mutableStateOf(TextFieldValue()) }

    /***
     * TODO: Let user add new positions as necessary
     * i.e. tap a button at the buttom of the screen that lets you add another player
     * add a dropdown menu next to each one of these new textfields that determines the position type
     * after tapping the suggestion, change field to immutable and put an x next to it to remove the player
     * starts with mutable text field with a dropdown next to it
     * tap of suggestion turns it into an immutable textfield/view with an x next to it
     * should make for easier creation of the league
     * league name at the top of the screen
     */

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        item {
            OutlinedTextField(
                value = leagueName,
                onValueChange = { leagueName = it },
                label = {
                    Text("League Name")
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(
            items = viewModel.selectedPlayers,
            key = { it.id }
        ) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.position.title, modifier = Modifier.padding(end = 8.dp))
                OutlinedTextField(
                    value = TextFieldValue("${item.player.firstName} ${item.player.lastName}"),
                    onValueChange = {},
                    label = {
                        Text("Player Name")
                    },
                    enabled = false,
                )
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        viewModel.selectedPlayers.remove(item)
                    }
                )
            }
        }


        items(
            items = viewModel.addPlayerFields,
            key = { it.id }
        ) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                PositionDropdownMenu {
                    item.position = it
                }
                Column {
                    OutlinedTextField(
                        value = item.textFieldValue.value,
                        onValueChange = {
                            item.textFieldValue.value = it
                            if (item.textFieldValue.value.text.length > 2) {
                                viewModel.getSuggestions(item)
                            }
                        },
                        label = {
                            Text("Player Name")
                        }
                    )
                    item.suggestions.forEach {
                        Text(
                            "${it.firstName} ${it.lastName}",
                            modifier = Modifier.clickable {
                                viewModel.selectedPlayers.add(
                                    SelectedPlayer(
                                        player = it,
                                        position = item.position
                                    )
                                )
                                viewModel.addPlayerFields.remove(item)
                            }
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        viewModel.addPlayerFields.remove(item)
                    }
                )
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.addPlayerFields.add(PlayerSelectionField())
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Add Player")
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.saveLeague()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = ""
                )
                Text("Save & Finish")
            }
        }
    }
}

@Composable
fun PositionDropdownMenu(onPositionSelectionChanged: (Position) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(Position.QB) }

    fun setPosition(position: Position) {
        onPositionSelectionChanged(position)
        selectedPosition = position
        expanded = false
    }

    Row {
        Column {
            Button(onClick = { expanded = true }) {
                Text(selectedPosition.title)
                Icon(Icons.Filled.ArrowDropDown, "")
            }

            if (expanded) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(onClick = { setPosition(Position.QB) }) {
                        Text(Position.QB.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.RB) }) {
                        Text(Position.RB.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.WR) }) {
                        Text(Position.WR.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.TE) }) {
                        Text(Position.TE.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.FLEX) }) {
                        Text(Position.FLEX.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.K) }) {
                        Text(Position.K.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.DST) }) {
                        Text(Position.DST.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.SuperFLEX) }) {
                        Text(Position.SuperFLEX.title)
                    }
                    DropdownMenuItem(onClick = { setPosition(Position.Bench) }) {
                        Text(Position.Bench.title)
                    }
                }
            }
        }

    }
}