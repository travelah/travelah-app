package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun SubHeaderHome(
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title, style = MaterialTheme.typography.subtitle2.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier=Modifier.padding(0.dp)
        )
        TextButton(onClick = onClick) {
            Text(
                "See More", style = MaterialTheme.typography.subtitle2.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier=Modifier.padding(0.dp)
            )
        }
    }
}