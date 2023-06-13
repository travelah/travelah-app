package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.response.ChatItem

@Composable
fun BubbleChat(chat: ChatItem, isQuestion: Boolean = true, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            if (!isQuestion) Alignment.Start else Alignment.End
        ),
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth(0.7f)
            .padding(12.dp)
    ) {
        AsyncImage(
            model = stringResource(id = R.string.profile_picture_link),
            contentDescription = stringResource(R.string.profile_image_content_desc),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_baseline_person_black_24),
            modifier = Modifier.size(40.dp)
        )
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color = Color(0xFFE8F7FD))
                .padding(16.dp)
        ) {
            Text(
                if (isQuestion) chat.question else chat.response,
                style = MaterialTheme.typography.caption
            )
        }
    }
}