package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.ui.composable.*
import com.amped94.ffbtracker.ui.theme.FFBTrackerTheme
import com.amped94.ffbtracker.util.screenIsShowing

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.fitsSystemWindows = true

        setContent {
            FFBTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main()
                }
            }
        }
    }

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
                        CreateLeague(viewModel)
                    }
                    composable(
                        Screen.Leagues.Edit.route,
                        arguments = listOf(
                            navArgument("leagueId") { type = NavType.LongType }
                        )
                    ) {
                        val leagueId = currentBackstack?.arguments?.getLong("leagueId") ?: 0
                        EditLeague(mainViewModel = viewModel, leagueId = leagueId)
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        FFBTrackerTheme {
            Text("Test")
        }
    }
}