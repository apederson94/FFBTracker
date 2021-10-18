package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.LeaguesViewModel
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

@Composable
fun Leagues(mainViewModel: MainViewModel, navController: NavController) {
    val viewModel by remember { mutableStateOf(LeaguesViewModel()) }
    val leaguesAndPlayers by viewModel.leaguesAndPlayers.observeAsState()

    mainViewModel.onFABTapped.value = {
        navController.navigate(Screen.Leagues.Create.route) {
            restoreState = true
        }
    }

    mainViewModel.title.value = "Leagues"

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        leaguesAndPlayers?.let {
            items(it) { data ->
                Spacer(Modifier.height(4.dp))
                LeagueCard(data, onEditClicked = { league ->
                    navController.navigate(
                        Screen.Leagues.Edit.route.replace(
                            "{leagueId}",
                            "${league.leagueId}"
                        )
                    )
                })
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}