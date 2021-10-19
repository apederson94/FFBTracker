package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun PlayersList(viewModel: MainViewModel) {

    viewModel.title.value = "Players"

    if (viewModel.playersAndLeagues.isEmpty()) {
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
            items(viewModel.playersAndLeagues.sortedBy { it.player.lastName }) { item ->
                Row(verticalAlignment = Alignment.CenterVertically) {
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
        }
    }

}