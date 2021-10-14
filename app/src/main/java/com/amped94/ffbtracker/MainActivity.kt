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
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
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
}

sealed class Screen(val route: String, val img: ImageVector, val title: String) {
    object Account : Screen("account", Icons.Filled.AccountCircle, "Account")
    object Players : Screen("players", Icons.Filled.Person, "Players")
    object AddLeague : Screen("addLeague", Icons.Filled.AddCircle, "Add League")
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

@Composable
fun Topbar(screen: Screen) {

    TopAppBar(title = {
        screen.title
    })
}

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navItems = listOf(Screen.Account, Screen.Players, Screen.AddLeague)

    BottomAppBar {
        navItems.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(screen.img, screen.title)
                }
            )
        }
    }
}

@Composable
fun PlayersList() {
    val viewModel by remember { mutableStateOf(MainViewModel()) }
    val playersAndLeagues by viewModel.playersAndLeagues.observeAsState()

    Column {
        playersAndLeagues?.let { playersAndLeagues ->
            UserInfo(playersAndLeagues)
        } ?: Text("Loading...")
    }
}

@Composable
fun UserInfo(playerData: List<PlayerAndLeagues>) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        playerData.sortedBy { it.player.lastName }.forEach { player ->
            item {
                Text("${player.player.lastName}, ${player.player.firstName}")

                player.leagues.forEach {
                    Text(it.name, modifier = Modifier.padding(start = 64.dp))
                }
            }
        }
    }
}

@Composable
fun CustomText(title: String, content: String) {
    Row {
        Text(title)
        Text(content)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FFBTrackerTheme {
        CustomText(title = "TEST", content = "TEST2")
    }
}