package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.amped94.ffbtracker.data.model.viewModel.PlayerSelectionFieldModel

@Composable
fun PlayerSelectionRow(
    item: PlayerSelectionFieldModel,
    getSuggestions: () -> Unit,
    addSelectedPlayer: () -> Unit,
    removeRow: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        AddPlayerPositionDropdown {
            item.position = it
            if (item.textFieldValue.value.text.length > 2) {
                getSuggestions()
            }
        }

        PlayerSelectionField(
            item = item,
            getSuggestions = getSuggestions,
            addSelectedPlayer = addSelectedPlayer
        )

        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "",
            modifier = Modifier.clickable {
                removeRow()
            }
        )
    }
}