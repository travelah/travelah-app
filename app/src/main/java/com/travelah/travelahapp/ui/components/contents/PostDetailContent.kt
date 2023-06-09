package com.travelah.travelahapp.ui.components.contents

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.ui.components.elements.IconWithCount

@Composable
fun PostDetailContent(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val configuration = LocalConfiguration.current
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box {
            AsyncImage(
                model = "https://images.pexels.com/photos/1766838/pexels-photo-1766838.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
                contentDescription = stringResource(id = R.string.profile_image_content_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 280.dp else 320.dp
                )
            )
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onBackClick() }
            )
        }
        Column(
            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = stringResource(id = R.string.profile_picture_link),
                    contentDescription = stringResource(id = R.string.profile_image_content_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(
                        text = "Zuhal 'Alimul Hadi", style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "1 day ago",
                        style = MaterialTheme.typography.caption,
                        color = Color(0xFF737373)
                    )
                }
            }
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In egestas ultrices dolor venenatis sodales. Nam in sem eget purus scelerisque semper. Sed fermentum odio sit amet ex cursus, sed convallis velit vulputate. Maecenas sollicitudin pretium tellus non consequat.  Curabitur quis aliquet dolor, placerat vulputate dui. Fusce cursus gravida nunc, vel fermentum nunc lobortis et. Integer augue urna, congue a tempus in, interdum id lectus. Donec quis ullamcorper urna, nec sagittis leo. Fusce eleifend molestie metus, at luctus sem vehicula vitae.",
                style = MaterialTheme.typography.body2
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconWithCount(
                    icon = R.drawable.ic_baseline_comment_24,
                    contentDescription = stringResource(R.string.comment_count),
                    count = "3",
                    iconSize = 24.dp,
                    textStyle = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconWithCount(
                        icon = if (true) R.drawable.ic_baseline_thumb_up_travelah_blue_24 else R.drawable.ic_baseline_thumb_up_24,
                        contentDescription = stringResource(R.string.like_count),
                        count = "2",
                        onClick = { },
                        iconSize = 24.dp,
                        textStyle = MaterialTheme.typography.caption.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconWithCount(
                        icon = if (false) R.drawable.ic_baseline_thumb_down_travelah_blue_24 else R.drawable.ic_baseline_thumb_down_24,
                        contentDescription = stringResource(R.string.dislike_count),
                        count = "3",
                        onClick = { },
                        iconSize = 24.dp,
                        textStyle = MaterialTheme.typography.caption.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}