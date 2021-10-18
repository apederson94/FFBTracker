package com.amped94.ffbtracker.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.EditLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

@Composable
fun EditLeague(mainViewModel: MainViewModel, leagueId: Long, navController: NavController) {
    val viewModel by remember { mutableStateOf(EditLeagueViewModel(leagueId)) }

    mainViewModel.title.value = "Edit League"

    mainViewModel.onFABTapped.value = {
        viewModel.onFABTapped()
    }

    viewModel.onSaveFinished = {
        navController.navigate(Screen.Leagues.route)
    }

    viewModel.leagueAndPlayers.value?.let {
        EditableLeague(viewModel)
    }
}