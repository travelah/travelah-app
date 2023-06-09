package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconWithCount(
    icon: Int,
    contentDescription: String,
    count: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    iconSize: Dp? = null,
    textStyle: TextStyle? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize ?: 16.dp)
        )
        Text(
            count, style = textStyle ?: MaterialTheme.typography.overline.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}