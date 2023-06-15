package com.travelah.travelahapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
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
import com.travelah.travelahapp.ui.components.elements.ConfirmDialog
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
    val showModal = remember { mutableStateOf(false) }
    val isOffline = remember { mutableStateOf(false) }

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

    if (showModal.value) {
        ConfirmDialog(
            confirmButton = {
                TextButton(onClick = {
                    deletePost(
                        if (isOffline.value) postFromActivity?.id
                            ?: 0 else if (result is Result.Success) result.data.id else 0
                    )
                    onBackClick()
                })
                { Text(text = stringResource(R.string.ok), color = Color(0xFF07AFFF)) }
            },
            dismissButton = {
                TextButton(onClick = { showModal.value = false })
                { Text(text = stringResource(R.string.cancel), color = Color(0xFF07AFFF)) }
            },
            title = {
                Text(
                    text = stringResource(R.string.delete_post),
                    color = Color.Black
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.delete_post_confirmation,
                    ),
                    color = Color.Black
                )
            },
        )
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
                            IconButton(onClick = {
                                showModal.value = true
                                isOffline.value = false
                            }
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
                            IconButton(onClick = {
                                showModal.value = true
                                isOffline.value = true
                            }
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