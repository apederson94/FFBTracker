package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddLeague() {
    val leagueName = remember { mutableStateOf(TextFieldValue()) }

    Column {
        Text("League Name")
        TextField(value = leagueName.value, onValueChange = { leagueName.value = it } )
    }
}