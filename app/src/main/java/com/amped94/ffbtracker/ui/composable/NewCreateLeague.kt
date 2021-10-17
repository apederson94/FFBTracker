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
import com.amped94.ffbtracker.data.model.viewModel.*

@Composable
fun NewCreateLeague(mainViewModel: MainViewModel) {
    val viewModel by remember { mutableStateOf(NewCreateLeagueViewModel()) }
    mainViewModel.onFABTapped.value = {
        viewModel.addPlayerFields.add(PlayerSelectionField())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        item {
            OutlinedTextField(
                value = viewModel.leagueName.value,
                onValueChange = { viewModel.leagueName.value = it },
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
            SelectedPlayerRow(item = item, viewModel = viewModel)
        }

        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.addPlayerFields.add(PlayerSelectionField())
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Add Player")
                }
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
}

@Composable
fun SelectedPlayerRow(item: PlayerSelectionField, viewModel: NewCreateLeagueViewModel) {
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
                    } else {
                        item.suggestions.clear()
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

@Composable
fun PositionDropdownMenu(onPositionSelectionChanged: (Position) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(Position.QB) }

    Row {
        Column {
            Button(onClick = { expanded = true }) {
                Text(selectedPosition.title)
                Icon(Icons.Filled.ArrowDropDown, "")
            }

            if (expanded) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Position.values().forEach { position ->
                        DropdownMenuItem(onClick = {
                            onPositionSelectionChanged(position)
                            selectedPosition = position
                            expanded = false
                        }) {
                            Text(position.title)
                        }
                    }
                }
            }
        }

    }
}