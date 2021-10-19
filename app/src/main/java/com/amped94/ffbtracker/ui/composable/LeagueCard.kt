package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun LeagueCard(
    data: LeagueAndPlayers,
    editLeague: () -> Unit,
    deleteLeague: () -> Unit
) {
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
                    fontSize = 24.sp
                )

                if (data.league.type == FantasyProvider.Custom) {
                    Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit League",
                            modifier = Modifier.clickable {
                                editLeague()
                            }
                        )
                        Spacer(Modifier.width(24.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete League",
                            modifier = Modifier.clickable {
                                deleteLeague()
                            }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }

            if (!isCollapsed.value) {
                val sortedPlayers = data.players.sortedBy { it.lastName }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(start = 32.dp)) {
                        sortedPlayers.forEach { player ->
                            val position = Position.getFromString(player.position)
                            PositionText(position = position)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        sortedPlayers.forEach { player ->
                            Text("${player.lastName}, ${player.firstName}")
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        sortedPlayers.forEach { player ->
                            Text(
                                text = player.team,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PositionText(position: Position) {
    Text(
        text = position.title,
        color = position.backgroundColor,
    )
}