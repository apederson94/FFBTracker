package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.amped94.ffbtracker.data.model.viewModel.PlayerSelectionFieldModel

@Composable
fun PlayerSelectionField(
    item: PlayerSelectionFieldModel,
    getSuggestions: () -> Unit,
    addSelectedPlayer: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = item.textFieldValue.value,
            onValueChange = {
                item.textFieldValue.value = it
                if (item.textFieldValue.value.text.length > 2) {
                    getSuggestions()
                } else {
                    item.suggestions.clear()
                }
            },
            label = {
                Text("Player Name")
            }
        )
        item.suggestions.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "${it.firstName} ${it.lastName}",
                    modifier = Modifier
                        .clickable {
                            item.player = it
                            addSelectedPlayer()
                        }
                        .padding(8.dp)
                )
                Text(it.team, color = MaterialTheme.colors.onSurface)
            }
        }
    }
}