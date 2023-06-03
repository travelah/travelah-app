package com.travelah.travelahapp.ui.components.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import coil.util.DebugLogger
import com.travelah.travelahapp.R

@Composable
fun PostCardHome(
    profPic: String?,
    username: String,
    title: String,
    date: String,
    likeCount: Int,
    modifier: Modifier = Modifier
) {
    val imageLoader = LocalContext.current.imageLoader.newBuilder()
        .logger(DebugLogger())
        .build()

    Box(
        modifier = modifier
            .height(112.dp)
            .fillMaxWidth()
            .background(color = Color(0xFFE2F0F7), shape = RoundedCornerShape(16.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                Alignment.CenterVertically
            ),
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(
                        profPic
                    ).build(),
                    contentDescription = stringResource(R.string.profile_image_content_desc),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_baseline_person_black_24),
                    error = painterResource(id = R.drawable.ic_baseline_person_black_24),
                    modifier = Modifier.size(20.dp),
                    imageLoader = imageLoader
                )
                Text(
                    username,
                    style = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.body2.copy(
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                date,
                style = MaterialTheme.typography.caption.copy(
                    color = Color(0xFF737373)
                ),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_thumb_up_24),
                    contentDescription = stringResource(R.string.tips),
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    likeCount.toString(), style = MaterialTheme.typography.overline.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}