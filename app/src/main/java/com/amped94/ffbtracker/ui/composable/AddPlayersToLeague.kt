package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.amped94.ffbtracker.data.model.viewModel.AddPlayersToLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun AddPlayersToLeague() {
    val viewModel by remember { mutableStateOf(AddPlayersToLeagueViewModel()) }
    val league by viewModel.league.observeAsState()

    LazyColumn {
        league?.numQB?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX.QB)
            }
        }
        league?.numRB?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX.FLEX.RB)
            }
        }
        league?.numWR?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX.FLEX.WR)
            }
        }
        league?.numTE?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX.FLEX.TE)
            }
        }
        league?.numFLEX?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX.FLEX)
            }
        }
        league?.numK?.let {
            items(it) {
                PlayerField(type = Position.K)
            }
        }
        league?.numDST?.let {
            items(it) {
                PlayerField(type = Position.DST)
            }
        }
        league?.numSuperFLEX?.let {
            items(it) {
                PlayerField(type = Position.SuperFLEX)
            }
        }
        league?.numBench?.let {
            items(it) {
                PlayerField(type = Position.Bench)
            }
        }

        item {
            Button(onClick = {
                viewModel.savePlayersToLeague(emptyList())
            }) {
                Text("Save & Finish")
            }
        }
    }
}