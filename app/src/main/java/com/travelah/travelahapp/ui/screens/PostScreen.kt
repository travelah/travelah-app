package com.travelah.travelahapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.ui.components.contents.PostContent

@Composable
fun PostScreen(
    posts: LazyPagingItems<PostEntity>,
    token: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = posts.loadState) {
        if (posts.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (posts.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (posts.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF07AFFF)
            )
        } else {
            PostContent(
                posts = posts, token = token,
                modifier = modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 52.dp)
                    .fillMaxWidth(),
            )
        }
    }
}