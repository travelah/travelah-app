package com.travelah.travelahapp.ui.components.elements

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    onDismissRequest: () -> Unit = {},
    confirmButton: @Composable () -> Unit = {
        TextButton(onClick = {})
        { Text(text = "OK") }
    },
    dismissButton: @Composable (() -> Unit)? = {
        TextButton(onClick = {})
        { Text(text = "Cancel") }
    },
    title: @Composable (() -> Unit)? = { },
    text: @Composable (() -> Unit)? = { },
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        title = title,
        text = text
    )
}