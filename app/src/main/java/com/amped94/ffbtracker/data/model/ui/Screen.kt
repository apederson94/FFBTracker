package com.amped94.ffbtracker.data.model.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val img: ImageVector, val title: String) {
    object Account : Screen(
        route = "account",
        img = Icons.Filled.AccountCircle,
        title = "Account"
    )
    object Players : Screen(
        route = "players",
        img = Icons.Filled.Person,
        title = "Players"
    )
    object AddLeague : Screen(
        route = "addLeague",
        img = Icons.Filled.AddCircle,
        title = "Add League"
    )
}