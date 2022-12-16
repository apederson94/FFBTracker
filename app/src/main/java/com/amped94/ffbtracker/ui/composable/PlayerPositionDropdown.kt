package com.amped94.ffbtracker.ui.composable

import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.amped94.ffbtracker.data.model.viewModel.MainViewModel
import com.amped94.ffbtracker.data.model.viewModel.Position

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPositionDropdown(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(128.dp)
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = viewModel.selectedPosition.value?.title ?: "Any",
            onValueChange = {},
            label = { Text("Position") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            textStyle = TextStyle(
                color = viewModel.selectedPosition.value?.backgroundColor
                    ?: MaterialTheme.colorScheme.onSurface
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("Any") }, onClick = {
                viewModel.filterPlayers(position = null)
                expanded = false
            })
            Position.values().forEach {
                DropdownMenuItem(text = {
                    Text(
                        it.title,
                        color = it.backgroundColor
                    )
                }, onClick = {
                    viewModel.filterPlayers(position = it)
                    expanded = false
                })
            }
        }
    }
}
