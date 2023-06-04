package com.travelah.travelahapp.ui.components.contents

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.ui.components.elements.PostCard
import com.travelah.travelahapp.utils.withDateFormatFromISO
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun PostContent(
    posts: LazyPagingItems<PostEntity>,
    token: String,
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun likeDislikePost(id: Int, isLike: Boolean) {
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
                        posts.refresh()
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
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(posts, key = { it.id }) { post ->
            if (post != null) {
                PostCard(
                    username = post.posterFullName,
                    profPic = post.profilePicOfUser,
                    title = post.description,
                    date = post.createdAt.withDateFormatFromISO(),
                    likeCount = post.likeCount,
                    dontLikeCount = post.dontLikeCount,
                    commentCount = post.commentCount,
                    isUserLike = post.isUserLike,
                    isUserDontLike = post.isUserDontLike,
                    hideDislike = false,
                    onClickLike = {
                        likeDislikePost(post.id, true)
                    },
                    onClickDontLike = {
                        likeDislikePost(post.id, false)
                    }
                )
            }
        }
        item {
            if (posts.loadState.append is LoadState.Loading) {
                CircularProgressIndicator()
            }
        }
    }
}