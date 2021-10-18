package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.League
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
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

@Composable
fun LeagueCard(data: LeagueAndPlayers, onEditClicked: (League) -> Unit) {
    val isCollapsed = remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.DarkGray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { isCollapsed.value = !isCollapsed.value },
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    data.league.name,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
                    fontSize = 18.sp
                )

                if (data.league.type == FantasyProvider.Custom) {
                    Row(modifier = Modifier.align(CenterVertically)) {
                        Icon(Icons.Default.Edit, "Edit League", modifier = Modifier
                            .clickable {
                                onEditClicked(data.league)
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }

            if (!isCollapsed.value) {
                data.players.sortedBy { it.lastName }.forEach { player ->
                    Text(
                        "${player.lastName}, ${player.firstName}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}