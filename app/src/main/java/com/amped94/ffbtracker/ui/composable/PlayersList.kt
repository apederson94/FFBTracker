package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersList(viewModel: MainViewModel) {
    val searchText = remember { mutableStateOf("") }
    val selectedPosition: MutableState<Position?> = remember { mutableStateOf(null) }

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
            item {
                OutlinedTextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                        viewModel.searchPlayers(it)
                    },
                    label = { Text("Search") },
                    modifier = Modifier.padding(8.dp).fillMaxWidth()
                )
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
            } else {
                item {
                    Text("No players to show...")
                }
            }
        }
    }

}