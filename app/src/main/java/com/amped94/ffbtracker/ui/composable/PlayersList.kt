package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

@Composable
fun PlayersList(viewModel: MainViewModel) {
    val playersAndLeagues by viewModel.playersAndLeagues.observeAsState()

    playersAndLeagues?.let { data ->
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            data.sortedBy { it.player.lastName }.forEach { item ->
                item {
                    Text("${item.player.lastName}, ${item.player.firstName}")

                    item.leagues.forEach {
                        Text(it.name, modifier = Modifier.padding(start = 64.dp))
                    }
                }
            }
        }
    } ?: Text("Loading...")
}