package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.MainApplication
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel

@Composable
fun Account(mainViewModel: MainViewModel) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext())
    val initialUsername = prefs.getString("sleeperUsername", "") ?: ""
    val username = remember { mutableStateOf(TextFieldValue(initialUsername)) }

    mainViewModel.title.value = "Account"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Sleeper Username") },
            modifier = Modifier.padding(8.dp)
        )
        Button(onClick = {
            prefs.edit().putString("sleeperUsername", username.value.text).apply()
        }) {
            Text("Save")
        }
    }
}