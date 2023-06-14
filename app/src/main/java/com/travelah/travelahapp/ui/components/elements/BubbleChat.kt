package com.travelah.travelahapp.ui.components.elements

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.mappers.toPost
import com.travelah.travelahapp.data.remote.models.Places
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.view.maps.MapsActivity
import com.travelah.travelahapp.view.post.PostDetailActivity

@Composable
fun BubbleChat(
    chat: ChatItem,
    isQuestion: Boolean = true,
    onClickAlt: () -> Unit = {},
    onClickAlt2: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun onRecommendationClick(places: Places) {
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra(MapsActivity.EXTRA_PLACES, places)
        context.startActivity(intent)
    }

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
                modifier = Modifier.size(40.dp),
                error = painterResource(id = R.drawable.ic_baseline_person_black_24)
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
        if (chat.altIntent1 != null && chat.altIntent2 != null && (chat.chatType == 1)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .height(96.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                PrimaryButton(
                    onClick = { onClickAlt() },
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            text = chat.altIntent1, style = MaterialTheme.typography.caption.copy(
                                fontSize = 10.sp,
                                color = Color.White,
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                )
                PrimaryButton(
                    onClick = { onClickAlt2() },
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            text = chat.altIntent2, style = MaterialTheme.typography.caption.copy(
                                fontSize = 10.sp,
                                color = Color.White
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    },
                )
            }
        }
        if (chat.places.isNotEmpty() && chat.chatType == 2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                for (place in chat.places) {
                    key("${place.lat} ${place.lng}") {
                        PrimaryButton(
                            onClick = { onRecommendationClick(place) },
                            content = {
                                Text(
                                    text = place.place,
                                    style = MaterialTheme.typography.caption.copy(
                                        fontSize = 10.sp,
                                        color = Color.White
                                    )
                                )
                            }, modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}