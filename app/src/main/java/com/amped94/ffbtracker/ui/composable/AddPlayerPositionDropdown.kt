package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import com.amped94.ffbtracker.data.model.viewModel.Position

@Composable
fun AddPlayerPositionDropdown(onPositionSelectionChanged: (Position) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPosition by remember { mutableStateOf(Position.QB) }

    Row {
        Column {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    contentColor = selectedPosition.backgroundColor
                )
            ) {
                Text(selectedPosition.title)
                Icon(Icons.Filled.ArrowDropDown, "")
            }

            if (expanded) {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    Position.values().forEach { position ->
                        DropdownMenuItem(
                            text = { Text(position.title, color = position.backgroundColor) }
                            , onClick = {
                                onPositionSelectionChanged(position)
                                selectedPosition = position
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

    }
}