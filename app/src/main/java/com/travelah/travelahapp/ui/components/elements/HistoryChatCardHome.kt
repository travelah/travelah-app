package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun HistoryChatCardHome(
    latestChat: String,
    date: String,
    onClickSeeChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(76.dp)
            .clickable {
                onClickSeeChat()
            }
            .fillMaxWidth()
            .background(color = Color(0xFFE2F0F7), shape = RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                Alignment.CenterVertically
            ), modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = latestChat,
                color = Color(0xFF343434),
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                date,
                color = Color(0xFF737373),
                style = MaterialTheme.typography.caption.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}