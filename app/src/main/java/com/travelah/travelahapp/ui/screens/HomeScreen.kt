package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.main.MainViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.ui.components.contents.HomeContent
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.response.HistoryChat
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.elements.ErrorText
import com.travelah.travelahapp.view.post.PostViewModel

@Composable
fun HomeScreen(
    postResult: Result<List<Post>>,
    chatResult: Result<List<HistoryChat>>,
    onClickSeeChat: () -> Unit,
    onClickSeePost: () -> Unit,
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    postViewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    token: String,
    modifier: Modifier = Modifier,
) {
    val profileState: State<Profile?> = viewModel.getProfile().observeAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when (chatResult) {
            is Result.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF07AFFF)
                )
            }
            is Result.Success -> {
                when (postResult) {
                    is Result.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFF07AFFF)
                        )
                    }
                    is Result.Success -> {
                        HomeContent(
                            postViewModel = postViewModel,
                            listChat = chatResult.data,
                            listPost = postResult.data,
                            profileName = profileState.value?.fullName ?: "",
                            modifier = modifier
                                .padding(20.dp)
                                .fillMaxWidth(),
                            onClickSeeChat = onClickSeeChat,
                            onClickSeePost = onClickSeePost,
                            token = token
                        )
                    }
                    is Result.Error -> {
                        ErrorText(text = stringResource(id = R.string.failed_error))
                    }
                }
            }
            is Result.Error -> {
                ErrorText(text = stringResource(id = R.string.failed_error))
            }
        }
    }
}