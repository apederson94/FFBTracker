package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayersList(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }

    viewModel.title.value = "Players"

    if (viewModel.isLoading.value) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "Loading...",
                fontSize = 48.sp
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            stickyHeader {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = viewModel.searchText.value,
                        onValueChange = {
                            viewModel.searchPlayers(it)
                        },
                        label = { Text("Search") },
                        modifier = Modifier.fillMaxWidth(0.67f)
                        )
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                    ) {
                        TextField(
                            // The `menuAnchor` modifier must be passed to the text field for correctness.
                            modifier = Modifier.menuAnchor(),
                            readOnly = true,
                            value = viewModel.selectedPosition.value?.title ?: "Any",
                            onValueChange = {},
                            label = { Text("Position") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            textStyle = TextStyle(
                                color = viewModel.selectedPosition.value?.backgroundColor
                                    ?: MaterialTheme.colorScheme.onSurface
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("Any") }, onClick = {
                                viewModel.filterPlayers(null)
                                expanded = false
                            })
                            Position.values().forEach {
                                DropdownMenuItem(text = {
                                    Text(
                                        it.title,
                                        color = it.backgroundColor
                                    )
                                }, onClick = {
                                    viewModel.filterPlayers(it)
                                    expanded = false
                                })
                            }
                        }
                    }
                }
            }
            if (viewModel.playersAndLeaguesToShow.isNotEmpty()) {
                items(viewModel.playersAndLeaguesToShow.sortedBy { it.player.lastName }) { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        val position = Position.getFromString(item.player.position)
                        PositionText(position = position)
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = "${item.player.lastName}, ${item.player.firstName}",
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.width(16.dp))
                        Text(
                            text = item.player.team,
                            color = Color.Gray
                        )
                    }

                    item.leagues.forEach {
                        Text(
                            it.name, modifier = Modifier.padding(start = 64.dp),
                            fontSize = 16.sp
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                item {
                    Text("No players to show...")
                }
            }
        }
    }

}