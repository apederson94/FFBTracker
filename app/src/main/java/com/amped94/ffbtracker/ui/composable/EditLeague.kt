package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.amped94.ffbtracker.data.model.viewModel.EditLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

@Composable
fun EditLeague(mainViewModel: MainViewModel, leagueId: Long) {
    mainViewModel.title.value = "Edit League"

    val viewModel by remember { mutableStateOf(EditLeagueViewModel(leagueId)) }

    viewModel.leagueAndPlayers.value?.let { leagueAndPlayers ->
        LazyColumn {

        }
    }
}