package com.travelah.travelahapp.ui.components.contents

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Comment
import com.travelah.travelahapp.utils.withDateFormatFromISO
import com.travelah.travelahapp.view.ViewModelFactory
import com.travelah.travelahapp.view.post.PostViewModel
import kotlinx.coroutines.flow.catch
import com.travelah.travelahapp.data.Result
import kotlinx.coroutines.launch

@Composable
fun CommentsContent(
    comments: List<Comment> = mutableListOf(),
    id: Int,
    token: String,
    viewModel: PostViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    ),
    modifier: Modifier = Modifier,
) {
    var input by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    fun handleSubmit() {
        scope.launch {
            viewModel.createPostComment(input, id, token).catch {
                isLoading = false
                Toast.makeText(
                    context,
                    "Failed to create comments",
                    Toast.LENGTH_LONG
                ).show()
            }.collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoading = true
                    }
                    is Result.Success -> {
                        isLoading = false
                        viewModel.getAllPostComment(token, id)

                        Toast.makeText(
                            context,
                            "Success to create comments",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Result.Error -> {
                        isLoading = false
                        Toast.makeText(
                            context,
                            "Failed to create comments",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        val (boxRef, commentHeader, listComment) = createRefs()

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color(0xFF07AFFF)
                )
            }
        }

        Column(modifier = Modifier.constrainAs(commentHeader) {
            top.linkTo(parent.top, margin = 16.dp)
        }) {
            Box(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
            Divider(startIndent = 0.dp, thickness = 1.dp, color = Color(0xFFCAC8C8))
        }

        LazyColumn(
            modifier = Modifier
                .constrainAs(listComment) {
                    top.linkTo(commentHeader.bottom, margin = 12.dp)
                }
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(comments, key = { it.id }) { comment ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = comment.userProfilePicPath ?: "",
                            contentDescription = stringResource(id = R.string.profile_image_content_desc),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            error = painterResource(id = R.drawable.ic_baseline_person_black_24)
                        )
                        Column {
                            Text(
                                text = comment.userFullName,
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                text = comment.createdAt.withDateFormatFromISO(),
                                style = MaterialTheme.typography.caption,
                                color = Color(0xFF737373)
                            )
                        }
                    }
                    Text(
                        comment.description,
                        style = MaterialTheme.typography.body2
                    )
                    Divider(startIndent = 0.dp, thickness = 1.dp, color = Color(0xFFCAC8C8))
                }
            }
        }

        val boxModifier = Modifier
            .padding(top = 20.dp)
            .background(color = Color(0xFFA0D7FB))
            .padding(horizontal = 20.dp, vertical = 8.dp)

        Box(
            modifier = boxModifier
                .constrainAs(boxRef) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                placeholder =  { Text(stringResource(R.string.comment_text_field_placeholder)) },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            handleSubmit()
                        },
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color(0xFF55CBF7),
                    cursorColor = Color(0xFF55CBF7)
                ),
            )
        }
    }
}

@Preview
@Composable
fun CommentsContentPreview() {
    MaterialTheme {
        CommentsContent(mutableListOf(), 0, "")
    }
}