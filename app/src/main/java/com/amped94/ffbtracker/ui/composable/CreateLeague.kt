package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.EditableLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.PlayerSelectionFieldModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun CreateLeague(mainViewModel: MainViewModel, navController: NavController) {
    val viewModel by remember { mutableStateOf(EditableLeagueViewModel()) }

    mainViewModel.onFABTapped.value = {
        viewModel.onFABTapped()
    }

    mainViewModel.title.value = "Create A League"

    viewModel.onSaveFinished = {
        navController.navigate(Screen.Leagues.route)
    }

    EditableLeague(viewModel)
}

@Composable
fun EditableLeague(viewModel: EditableLeagueViewModel) {
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
                        viewModel.removeSelectedPlayer(item)
                    }
                )
            }
        }


        items(
            items = viewModel.playerSelectionFieldModels,
            key = { it.id }
        ) { item ->
            PlayerSelectionRow(
                item = item,
                getSuggestions = { viewModel.getSuggestions(item) },
                addSelectedPlayer = { viewModel.addNewlySelectedPlayer(item) },
                removeRow = { viewModel.removePlayerSelectionField(item) }
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Button(onClick = {
                    viewModel.saveLeague()
                }, modifier = Modifier.padding(vertical = 16.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save & Finish"
                    )
                    Text("Save & Finish")
                }
            }
        }
    }
}

@Composable
fun PlayerSelectionRow(
    item: PlayerSelectionFieldModel,
    getSuggestions: () -> Unit,
    addSelectedPlayer: () -> Unit,
    removeRow: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        PositionDropdownMenu {
            item.position = it
            item.textFieldValue.value = TextFieldValue()
            item.suggestions.clear()
        }

        PlayerSelectionField(
            item = item,
            getSuggestions = getSuggestions,
            addSelectedPlayer = addSelectedPlayer
        )

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "",
            modifier = Modifier.clickable {
                removeRow()
            }
        )
    }
}

@Composable
fun PlayerSelectionField(
    item: PlayerSelectionFieldModel,
    getSuggestions: () -> Unit,
    addSelectedPlayer: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = item.textFieldValue.value,
            onValueChange = {
                item.textFieldValue.value = it
                if (item.textFieldValue.value.text.length > 2) {
                    getSuggestions()
                } else {
                    item.suggestions.clear()
                }
            },
            label = {
                Text("Player Name")
            }
        )
        item.suggestions.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${it.firstName} ${it.lastName}",
                    modifier = Modifier
                        .clickable {
                            item.player = it
                            addSelectedPlayer()
                        }
                        .padding(8.dp)
                )
                Text(it.team, color = MaterialTheme.colors.onSurface)
            }
        }
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