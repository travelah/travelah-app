package com.travelah.travelahapp.ui.components.elements

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorText(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        style = MaterialTheme.typography.body2.copy(
            color = Color(0xFFDC362E),
            textAlign = TextAlign.Center
        ),
        modifier = modifier
    )
}