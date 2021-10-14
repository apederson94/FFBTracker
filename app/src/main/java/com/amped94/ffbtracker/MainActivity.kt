package com.amped94.ffbtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.data.model.db.entity.PlayerAndLeagues
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
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

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("FFBTracker")})
            },
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) {
            NavHost(navController = navController, startDestination = Screen.Players.route) {
                composable(Screen.Account.route) {
                    Text("Account")
                }
                composable(Screen.Players.route) {
                    PlayersList()
                }
                composable(Screen.AddLeague.route) {
                    Text("Add League")
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