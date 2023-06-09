package com.travelah.travelahapp.ui.components.contents

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.elements.IconWithCount
import com.travelah.travelahapp.utils.withDateFormatFromISO
import com.travelah.travelahapp.view.post.PostCommentActivity
import com.travelah.travelahapp.view.post.PostDetailActivity
import com.travelah.travelahapp.view.post.PostViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun PostDetailContent(
    token: String,
    result: Post?,
    postFromActivity: Post?,
    viewModel: PostViewModel,
    modifier: Modifier = Modifier,
) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val id = result?.id ?: postFromActivity?.id ?: 0

    var likeCount by remember {
        mutableStateOf(
            result?.likeCount ?: postFromActivity?.likeCount ?: 0
        )
    }
    var isUserLike by remember {
        mutableStateOf(
            result?.isUserLike ?: postFromActivity?.isUserLike ?: false
        )
    }
    var dontlikeCount by remember {
        mutableStateOf(
            result?.dontLikeCount ?: postFromActivity?.likeCount ?: 0
        )
    }
    var isUserDontLike by remember {
        mutableStateOf(
            result?.isUserDontLike ?: postFromActivity?.isUserDontLike ?: false
        )
    }

    fun handleOptimisticChanges(isLike: Boolean) {
        if (isLike) {
            if (!isUserLike) {
                likeCount += 1
                isUserLike = true
                if (isUserDontLike) {
                    dontlikeCount -= 1
                }
                isUserDontLike = false
            } else {
                likeCount -= 1
                isUserLike = false
            }
        } else {
            if (!isUserDontLike) {
                dontlikeCount += 1
                isUserDontLike = true
                if (isUserLike) {
                    likeCount -= 1
                }
                isUserLike = false
            } else {
                dontlikeCount -= 1
                isUserDontLike = false
            }
        }
    }

    fun likeDislikePost(id: Int, isLike: Boolean) {
        // to handle if the optimistic changes failed
        val likeCountTemp = likeCount
        val isUserLikeTemp = isUserLike
        val dontlikeCountTemp = dontlikeCount
        val isUserDontLikeTemp = isUserDontLike

        handleOptimisticChanges(isLike)

        scope.launch {
            viewModel.likeDislikePost(token, id, isLike).catch {
                Toast.makeText(
                    context,
                    "Error: " + it.message.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }.collect {
                when (it) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        Toast.makeText(
                            context,
                            "You ${if (isLike) "liked" else "disliked"} a post",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "Failed to ${if (isLike) "liked" else "disliked"} a post: ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()

                        // revert changes
                        likeCount = likeCountTemp
                        isUserLike = isUserLikeTemp
                        dontlikeCount = dontlikeCountTemp
                        isUserDontLike = isUserDontLikeTemp
                    }
                }
            }
        }
    }

    fun onCommentButtonClick(id: Int?) {
        val intent = Intent(context, PostCommentActivity::class.java)
        intent.putExtra(PostDetailActivity.EXTRA_ID, id ?: 0)

        context.startActivity(intent)
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box {
            AsyncImage(
                model = if (result != null) "https://storage.googleapis.com/travelah-storage/${result.postPhotoPath}/${result.postPhotoName}" else "https://storage.googleapis.com/travelah-storage/${postFromActivity?.postPhotoPath}/${postFromActivity?.postPhotoName}",
                contentDescription = stringResource(id = R.string.profile_image_content_desc),
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(
                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 280.dp else 320.dp
                ),
                error = painterResource(id = R.drawable.ic_place_holder)
            )
        }
        Column(
            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AsyncImage(
                    model = if (result != null) "https://storage.googleapis.com/travelah-storage/${result.profilePicOfUser}" else "https://storage.googleapis.com/travelah-storage/${postFromActivity?.profilePicOfUser}",
                    contentDescription = stringResource(id = R.string.profile_image_content_desc),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    error = painterResource(id = R.drawable.ic_baseline_person_black_24)
                )
                Column {
                    Text(
                        text = result?.posterFullName ?: "${postFromActivity?.posterFullName}",
                        style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = result?.createdAt?.withDateFormatFromISO()
                            ?: "${postFromActivity?.createdAt?.withDateFormatFromISO()}",
                        style = MaterialTheme.typography.caption,
                        color = Color(0xFF737373)
                    )
                }
            }
            Text(
                result?.title ?: "${postFromActivity?.title}",
                style = MaterialTheme.typography.body1.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_baseline_location_on_24),
                    contentDescription = stringResource(id = R.string.location)
                )
                Text(
                    text = result?.location ?: postFromActivity?.location ?: stringResource(R.string.no_location),
                    style = MaterialTheme.typography.body2.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Text(
                result?.description ?: "${postFromActivity?.description}",
                style = MaterialTheme.typography.body2.copy(
                    textAlign = TextAlign.Start
                )
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconWithCount(
                    icon = R.drawable.ic_baseline_comment_24,
                    contentDescription = stringResource(R.string.comment_count),
                    count = "${result?.commentCount ?: postFromActivity?.commentCount ?: 0}",
                    iconSize = 24.dp,
                    textStyle = MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    onClick = { onCommentButtonClick(result?.id ?: postFromActivity?.id) }
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconWithCount(
                        icon = if (isUserLike) R.drawable.ic_baseline_thumb_up_travelah_blue_24 else R.drawable.ic_baseline_thumb_up_24,
                        contentDescription = stringResource(R.string.like_count),
                        count = "$likeCount",
                        onClick = { likeDislikePost(id, true) },
                        iconSize = 24.dp,
                        textStyle = MaterialTheme.typography.caption.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    IconWithCount(
                        icon = if (isUserDontLike) R.drawable.ic_baseline_thumb_down_travelah_blue_24 else R.drawable.ic_baseline_thumb_down_24,
                        contentDescription = stringResource(R.string.dislike_count),
                        count = "$dontlikeCount",
                        onClick = { likeDislikePost(id, false) },
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