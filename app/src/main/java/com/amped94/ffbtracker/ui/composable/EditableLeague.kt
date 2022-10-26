package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.amped94.ffbtracker.data.model.viewModel.EditableLeagueViewModel

@OptIn(ExperimentalMaterial3Api::class)
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
                Text(
                    text = item.position.title,
                    color = item.position.backgroundColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
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