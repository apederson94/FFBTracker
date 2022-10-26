package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.util.screenIsShowing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main() {
    val navController = rememberNavController()
    val viewModel by remember { mutableStateOf(MainViewModel()) }
    val currentBackstack by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(viewModel.title.value) })
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        floatingActionButton = {
            if (currentBackstack?.screenIsShowing(Screen.Leagues.View) == true
                || currentBackstack?.screenIsShowing(Screen.Leagues.Create) == true
                || currentBackstack?.screenIsShowing(Screen.Leagues.Edit) == true
            ) {
                FloatingActionButton(onClick = {
                    viewModel.onFABTapped.value()
                }) {
                    Icon(Icons.Filled.Add, "Add League")
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Players.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Account.route) {
                Account(viewModel)
            }
            composable(Screen.Players.route) {
                PlayersList(viewModel)
            }
            navigation(
                startDestination = Screen.Leagues.View.route,
                route = Screen.Leagues.route
            ) {
                composable(Screen.Leagues.View.route) {
                    Leagues(viewModel, navController)
                }
                composable(Screen.Leagues.Create.route) {
                    CreateLeague(viewModel, navController)
                }
                composable(
                    Screen.Leagues.Edit.route,
                    arguments = listOf(
                        navArgument("leagueId") { type = NavType.LongType }
                    )
                ) {
                    val leagueId = currentBackstack?.arguments?.getLong("leagueId") ?: 0
                    EditLeague(
                        mainViewModel = viewModel,
                        leagueId = leagueId,
                        navController = navController
                    )
                }
            }
        }
    }
}