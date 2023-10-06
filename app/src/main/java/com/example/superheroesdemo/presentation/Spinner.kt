package com.example.superheroesdemo.presentation


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun SpinnerDemo(modifier: Modifier = Modifier, onValueChanged: (String) -> Unit) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val items = listOf("Any", "Likes", "Dislikes")
    var selectedItem by remember { mutableStateOf(items[0]) }

    Box(
        modifier = Modifier
            .then(modifier)
            //.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    isDropdownExpanded = !isDropdownExpanded
                }
        ) {
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.Black
                ),
                value = TextFieldValue(selectedItem),
                onValueChange = {
                    selectedItem = it.text
                },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null, tint = Color.Black) },
                readOnly = true,
                enabled = false,
                textStyle = TextStyle(fontSize = 18.sp).copy(color = Color.Black)
            )
        }

        if (isDropdownExpanded) {
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = {
                    isDropdownExpanded = false
                }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item, style = TextStyle(fontSize = 18.sp)) },
                        onClick = {
                            selectedItem = item
                            isDropdownExpanded = false
                            onValueChanged(item)
                        }
                    )
                }
            }
        }
    }
}





