package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppBarChat(
    fullName: String,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {

    TopAppBar(
        modifier = modifier,
        backgroundColor = Color(0xFFA0D7FB),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        title = {
            Text(text = fullName)
        },
        actions = actions
    )
}