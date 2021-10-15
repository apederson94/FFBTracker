package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import com.amped94.ffbtracker.data.model.viewModel.LeaguesViewModel
import com.amped94.ffbtracker.data.model.viewModel.PlayerFieldViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun Leagues() {
    //TODO: create a VM
    //  VM will hold all the data for this screen
    //  VM will add/create/delete/edit other typed leagues
    val viewModel by remember { mutableStateOf(LeaguesViewModel()) }
    val leagueName by viewModel.leagueName.observeAsState("")

    Column {
        Text("Display Leagues Here")
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