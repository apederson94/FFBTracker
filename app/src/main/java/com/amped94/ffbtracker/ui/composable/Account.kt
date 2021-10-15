package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.preference.PreferenceManager
import com.amped94.ffbtracker.MainApplication

@Composable
fun Account() {
    val prefs = PreferenceManager.getDefaultSharedPreferences(MainApplication.getContext())
    val initialUsername = prefs.getString("sleeperUsername", "") ?: ""
    val username = remember { mutableStateOf(TextFieldValue(initialUsername)) }

    Column {
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text("Sleeper Username") }
        )
        Button(onClick = {
            prefs.edit().putString("sleeperUsername", username.value.text).apply()
        }) {
            Text("Save")
        }
    }
}