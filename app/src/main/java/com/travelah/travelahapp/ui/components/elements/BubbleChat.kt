package com.travelah.travelahapp.ui.components.elements

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Places
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.view.maps.MapsActivity

@Composable
fun BubbleChat(
    chat: ChatItem,
    isQuestion: Boolean = true,
    onClickAlt: () -> Unit = {},
    onClickAlt2: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

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
                    .widthIn(max = (configuration.screenWidthDp  * 0.7f).dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                clipboardManager.setText(AnnotatedString(chat.question))
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.message_copied),
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                    }
            ) {
                Text(
                    chat.question,
                    style = MaterialTheme.typography.caption
                )
            }
            AsyncImage(
                model = "https://storage.googleapis.com/travelah-storage/${chat.user.profilePicPath}/${chat.user.profilePicName}",
                contentDescription = stringResource(R.string.profile_image_content_desc),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_baseline_person_black_24),
                modifier = Modifier
                    .size(40.dp)
                    .defaultMinSize(40.dp)
                    .clip(CircleShape),
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
                painter = painterResource(id = R.drawable.vela),
                contentDescription = stringResource(R.string.chatbot_profile_desc),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color = Color(0xFFE8F7FD))
                    .padding(horizontal = if (chat.response.length > 160) 48.dp else 24.dp, vertical = 16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                clipboardManager.setText(AnnotatedString(chat.response))
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.message_copied),
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        )
                    }
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