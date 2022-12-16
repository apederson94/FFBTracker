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
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlayersList(viewModel: MainViewModel) {
    viewModel.title.value = "Players"
    var showFilters = remember { mutableStateOf(false) }

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
                Column(modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = viewModel.searchText.value,
                            onValueChange = {
                                viewModel.searchPlayers(it)
                            },
                            label = { Text("Search") },
                            modifier = Modifier.fillMaxWidth(0.75f)
                        )
                        Button(onClick = { showFilters.value = !showFilters.value }) {
                            Text("Filters")
                        }
                    }

                    if (showFilters.value) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            PlayerTeamDropdown(viewModel = viewModel)
                            Spacer(modifier = Modifier.width(8.dp))
                            PlayerPositionDropdown(viewModel = viewModel)
                        }
                    }
                    Divider(modifier = Modifier.padding(top = 8.dp))
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
                            color = MaterialTheme.colorScheme.surfaceVariant
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