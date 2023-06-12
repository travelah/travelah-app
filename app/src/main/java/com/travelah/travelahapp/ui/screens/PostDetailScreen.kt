package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.contents.PostDetailContent
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel

@Composable
fun PostDetailScreen(
    token: String,
    result: Result<Post>,
    postFromActivity: Post?,
    onBackClick: () -> Unit = {},
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (result) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF07AFFF)
                )
            }
            is Result.Success -> {
                PostDetailContent(
                    token = token,
                    result = result.data,
                    postFromActivity = postFromActivity,
                    onBackClick = onBackClick,
                    viewModel = viewModel,
                    modifier = modifier
                )
            }
            is Result.Error -> {
                PostDetailContent(
                    token = token,
                    result = null,
                    postFromActivity = postFromActivity,
                    onBackClick = onBackClick,
                    viewModel = viewModel,
                    modifier = modifier
                )
            }
        }
    }
}