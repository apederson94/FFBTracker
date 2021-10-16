package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.amped94.ffbtracker.data.model.db.FantasyProvider
import com.amped94.ffbtracker.data.model.db.entity.LeagueAndPlayers
import com.amped94.ffbtracker.data.model.viewModel.LeaguesViewModel
import com.amped94.ffbtracker.data.model.viewModel.PlayerFieldViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun Leagues() {
    //TODO: create a VM
    //  VM will hold all the data for this screen
    //  VM will add/create/delete/edit other typed leagues
    val viewModel by remember { mutableStateOf(LeaguesViewModel()) }
    val leaguesAndPlayers by viewModel.leaguesAndPlayers.observeAsState()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        leaguesAndPlayers?.let {
            items(it) { data ->
                Spacer(Modifier.height(4.dp))
                LeagueCard(data)
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun LeagueCard(data: LeagueAndPlayers) {
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

                            }
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }


            if (!isCollapsed.value) {
                data.players.forEach { player ->
                    Text(
                        "${player.firstName} ${player.lastName}",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun PlayerField(type: Position, viewModel: PlayerFieldViewModel) {
    val suggestions by viewModel.autofillSuggestions.observeAsState()
    val text by viewModel.text.observeAsState()

    Column {
        OutlinedTextField(
            value = text ?: "",
            onValueChange = {
                if (it.length > 2) {
                    viewModel.getAutofillSuggestions(it)
                }
                viewModel.text.postValue(it)
            },
            label = {
                Text(type.title)
            }
        )
        suggestions?.forEach {
            Button(onClick = { viewModel.selectPlayer(it) }) {
                Text("${it.firstName} ${it.lastName}")
            }
        }
    }
}