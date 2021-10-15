package com.amped94.ffbtracker.util

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.amped94.ffbtracker.data.model.ui.Screen

fun NavBackStackEntry.screenIsShowing(screen: Screen) : Boolean {
    return destination.route == screen.route
}

fun NavBackStackEntry.screenIsInStack(screen: Screen) : Boolean {
    return destination.hierarchy.any { it.route == screen.route }
}