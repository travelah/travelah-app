package com.travelah.travelahapp.ui.screens

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.ui.common.UiState
import com.travelah.travelahapp.ui.components.contents.CommentsContent
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel

@Composable
fun PostCommentScreen(
    id: Int = 0,
    token: String = "",
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    modifier: Modifier = Modifier
) {
    viewModel.postComments.collectAsState().value.let {
        when (it) {
            is UiState.Loading -> {
                viewModel.getAllPostComment(token, id)
                CircularProgressIndicator()
            }
            is UiState.Success -> {
                CommentsContent(
                    it.data,
                    modifier
                )
            }
            is UiState.Error -> {
                Text("Error")
            }
        }
    }
}