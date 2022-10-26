package com.amped94.ffbtracker.ui.composable

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amped94.ffbtracker.data.model.ui.Screen

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    var selectedItem by remember { mutableStateOf<Screen>(Screen.Players) }
    val navItems = listOf(Screen.Account, Screen.Players, Screen.Leagues)

    NavigationBar {
        navItems.forEach { screen ->
            NavigationBarItem(
                icon = { screen.img?.let { Icon(it, contentDescription = screen.title) } },
                label = { screen.title?.let { Text(it) } },
                selected = selectedItem == screen,
                onClick = {
                    selectedItem = screen
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
                }
            )
        }

    }
}