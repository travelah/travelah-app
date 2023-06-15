package com.travelah.travelahapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.contents.PostDetailContent
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.ui.components.elements.AppBarChat
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Composable
fun PostDetailScreen(
    token: String,
    profile: Profile,
    result: Result<Post>,
    postFromActivity: Post?,
    onBackClick: () -> Unit = {},
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun deletePost(id: Int) {
        scope.launch {
            viewModel.deletePost(token, id).catch {
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
                            "You deleted a post",
                            Toast.LENGTH_LONG
                        ).show()
                        onBackClick()
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            context,
                            "Failed to delete a post: ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (result) {
            is Result.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF07AFFF)
                    )
                }
            }
            is Result.Success -> {
                AppBarChat(
                    fullName = stringResource(R.string.post_detail),
                    onBackClick = onBackClick,
                    actions = {
                        if (profile.id == result.data.userId) {
                            IconButton(onClick = { deletePost(result.data.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                )
                PostDetailContent(
                    token = token,
                    result = result.data,
                    postFromActivity = postFromActivity,
                    viewModel = viewModel,
                    modifier = modifier
                )
            }
            is Result.Error -> {
                AppBarChat(
                    fullName = stringResource(R.string.post_detail),
                    onBackClick = onBackClick,
                    actions = {
                        if (profile.id == postFromActivity?.userId) {
                            IconButton(onClick = { deletePost(postFromActivity?.id ?: 0) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }
                    }
                )
                PostDetailContent(
                    token = token,
                    result = null,
                    postFromActivity = postFromActivity,
                    viewModel = viewModel,
                    modifier = modifier
                )
            }
        }
    }
}