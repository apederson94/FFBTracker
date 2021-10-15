package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.navigation.compose.composable
import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.ui.composable.Account
import com.amped94.ffbtracker.ui.composable.BottomBar
import com.amped94.ffbtracker.ui.composable.Leagues
import com.amped94.ffbtracker.ui.composable.PlayersList
import com.amped94.ffbtracker.ui.theme.FFBTrackerTheme
import com.amped94.ffbtracker.util.screenIsShowing

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                TopAppBar(title = { Text("FFBTracker") })
            },
            bottomBar = {
                BottomBar(navController = navController)
            },
            floatingActionButton = {
                if (currentBackstack?.screenIsShowing(Screen.Leagues.View) == true) {
                    FloatingActionButton(onClick = {
                        navController.navigate(Screen.Leagues.Add.route) {
                            restoreState = true
                        }
                    }) {
                        Icon(Icons.Filled.Add, "Add League")
                    }
                }
            },
        ) {
            NavHost(navController = navController, startDestination = Screen.Players.route) {
                composable(Screen.Account.route) {
                    Account()
                }
                composable(Screen.Players.route) {
                    PlayersList(viewModel)
                }
                navigation(startDestination = Screen.Leagues.View.route, route = Screen.Leagues.route) {
                    composable(Screen.Leagues.View.route) {
                        Leagues()
                    }
                    composable(Screen.Leagues.Add.route) {
                        Text("Add A League Here")
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