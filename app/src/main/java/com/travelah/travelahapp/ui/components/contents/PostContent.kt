package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.ui.components.elements.PostCard
import com.travelah.travelahapp.utils.withDateFormatFromISO

@Composable
fun PostContent(
    posts: LazyPagingItems<PostEntity>,
    modifier: Modifier = Modifier,
) {
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
                    hideDislike = false
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