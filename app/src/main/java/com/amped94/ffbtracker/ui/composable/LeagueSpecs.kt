package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amped94.ffbtracker.data.model.ui.Screen
import com.amped94.ffbtracker.data.model.viewModel.LeagueSpecsViewModel

@Composable
fun LeagueSpecs(navController: NavController) {
    val viewModel by remember { mutableStateOf(LeagueSpecsViewModel()) }
    var leagueName by remember { mutableStateOf(TextFieldValue()) }
    val numberOfQB by viewModel.numberOfQB.observeAsState()
    val numberOfRB by viewModel.numberOfRB.observeAsState()
    val numberOfWR by viewModel.numberOfWR.observeAsState()
    val numberOfTE by viewModel.numberOfTE.observeAsState()
    val numberOfFLEX by viewModel.numberOfFLEX.observeAsState()
    val numberOfK by viewModel.numberOfK.observeAsState()
    val numberOfDST by viewModel.numberOfDST.observeAsState()
    val numberOfSuperFLEX by viewModel.numberOfSuperFLEX.observeAsState()

    Column {
        OutlinedTextField(
            value = leagueName,
            onValueChange = { leagueName = it },
            label = {
                Text("League Name")
            }, modifier = Modifier.padding(8.dp))

        Column(modifier = Modifier.padding(start = 8.dp)) {
            PositionDropdown("$numberOfQB QB") {
                viewModel.numberOfQB.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfRB RB") {
                viewModel.numberOfRB.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfWR WR") {
                viewModel.numberOfWR.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfTE TE") {
                viewModel.numberOfTE.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfFLEX FLEX") {
                viewModel.numberOfFLEX.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfK K") {
                viewModel.numberOfK.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfDST D/ST") {
                viewModel.numberOfDST.postValue(it)
            }
            Spacer(Modifier.height(8.dp))
            PositionDropdown("$numberOfSuperFLEX SuperFLEX") {
                viewModel.numberOfSuperFLEX.postValue(it)
            }
        }

        Button(onClick = {
            navController.navigate(Screen.Leagues.Add.AddPlayersToLeague.route)
        }) {
            Text("Continue")
        }
    }
}

@Composable
fun PositionDropdown(title: String, onClick: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Button(onClick = { expanded = !expanded }) {
        Text(title)
        Icon(Icons.Filled.ArrowDropDown, "Expand")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        (0..10).forEach {
            DropdownMenuItem(onClick = {
                onClick(it)
                expanded = false
            }) {
                Text("$it")
            }
        }
    }
}