package com.travelah.travelahapp.ui.components.elements

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppBarChat(modifier: Modifier = Modifier, fullName: String) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier,
        backgroundColor = Color(0xFFA0D7FB),
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        title = {
            Text(text = fullName)
        },
        actions = {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = {}) {
                    Text(text = "Call me")
                }
            }
        }
    )
}