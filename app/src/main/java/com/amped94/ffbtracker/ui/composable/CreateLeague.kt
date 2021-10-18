package com.amped94.ffbtracker.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.EditableLeagueViewModel
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

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