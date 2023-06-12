package com.travelah.travelahapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.travelah.travelahapp.data.remote.models.Comment
import com.travelah.travelahapp.ui.components.contents.CommentsContent
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel

@Composable
fun PostCommentScreen(
    comments: LazyPagingItems<Comment>,
    id: Int = 0,
    token: String = "",
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = comments.loadState) {
        if (comments.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (comments.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (comments.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF07AFFF)
            )
        } else {
            CommentsContent(
                comments = comments,
                id = id,
                token = token,
                modifier = modifier
            )
        }
    }
}