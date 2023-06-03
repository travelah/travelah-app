package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF07AFFF),
        ),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
       content()
    }
}