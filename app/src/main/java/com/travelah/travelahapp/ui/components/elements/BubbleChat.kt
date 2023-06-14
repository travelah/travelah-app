package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.response.ChatItem

@Composable
fun BubbleChat(
    chat: ChatItem,
    isQuestion: Boolean = true,
    onClickAlt: () -> Unit = {},
    onClickAlt2: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (isQuestion) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.End
            ),
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color(0xFFE8F7FD))
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    chat.question,
                    style = MaterialTheme.typography.caption
                )
            }
            AsyncImage(
                model = stringResource(id = R.string.profile_picture_link),
                contentDescription = stringResource(R.string.profile_image_content_desc),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_baseline_person_black_24),
                modifier = Modifier.size(40.dp)
            )

        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.Start
            ),
            verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_app),
                contentDescription = "Chatbot profile image",
                modifier = Modifier.size(40.dp)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color(0xFFE8F7FD))
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text(
                    chat.response,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
    if (chat.altIntent1 != null && chat.altIntent2 != null && (chat.chatType == 1)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            PrimaryButton(onClick = { onClickAlt() }, content = {
                Text(
                    text = chat.altIntent1, style = MaterialTheme.typography.caption.copy(
                        fontSize = 10.sp,
                        color = Color.White
                    )
                )
            }, modifier = Modifier.weight(0.5f))
            PrimaryButton(
                onClick = { onClickAlt2() }, modifier = Modifier.weight(0.5f),
                content = {
                    Text(
                        text = chat.altIntent2, style = MaterialTheme.typography.caption.copy(
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    )
                },
                )
        }
    }
}