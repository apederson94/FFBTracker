package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.ui.composable.Account
import com.amped94.ffbtracker.ui.composable.AddLeague
import com.amped94.ffbtracker.ui.composable.BottomBar
import com.amped94.ffbtracker.ui.composable.PlayersList
import com.amped94.ffbtracker.ui.theme.FFBTrackerTheme

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
        val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext())
        val username = prefs.getString("sleeperUsername", "")
        val navController = rememberNavController()
        val viewModel by remember { mutableStateOf(MainViewModel()) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("FFBTracker") })
            },
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) {
            NavHost(navController = navController, startDestination = Screen.Players.route) {
                composable(Screen.Account.route) {
                    Account()
                }
                composable(Screen.Players.route) {
                    PlayersList(viewModel)
                }
                composable(Screen.Leagues.route) {
                    AddLeague()
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