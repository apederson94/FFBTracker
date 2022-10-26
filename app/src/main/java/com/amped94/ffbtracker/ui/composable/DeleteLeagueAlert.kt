package com.amped94.ffbtracker.ui.composable

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.amped94.ffbtracker.data.model.db.entity.League

@Composable
fun DeleteLeagueAlert(
    leagueToDelete: League?,
    dismissAlert: () -> Unit,
    confirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { dismissAlert() },
        title = { Text("Delete League?") },
        text = {
            val leagueName = leagueToDelete?.name
            Text("Are you sure you want to delete \"$leagueName\"?")
        },
        confirmButton = {
            Button(
                onClick = {
                    confirmDelete()
                }
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    dismissAlert()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}