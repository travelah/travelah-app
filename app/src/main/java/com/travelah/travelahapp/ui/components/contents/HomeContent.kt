package com.travelah.travelahapp.ui.components.contents

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.ui.components.elements.*
import com.travelah.travelahapp.utils.withDateFormatFromISO
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.travelah.travelahapp.data.remote.models.HistoryChat
import com.travelah.travelahapp.view.chat.DetailChatActivity
import com.travelah.travelahapp.view.post.PostCommentActivity
import com.travelah.travelahapp.view.post.PostDetailActivity

@Composable
fun HomeContent(
    listChat: List<HistoryChat> = mutableListOf(),
    listPost: List<Post> = mutableListOf(),
    onClickSeeChat: () -> Unit,
    onClickSeePost: () -> Unit,
    profileName: String = "",
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    fun getHeightPost(): Dp {
        return when (listPost.size) {
            1 -> 160.dp
            2 -> 284.dp
            3 -> 416.dp
            else -> 80.dp
        }
    }

    fun getHeightChat(): Dp {
        return when (listChat.size) {
            1 -> 76.dp
            2 -> 164.dp
            3 -> 252.dp
            else -> 20.dp
        }
    }

    fun onPostCardClick(post: Post) {
        val intent = Intent(context, PostDetailActivity::class.java)
        intent.putExtra(PostDetailActivity.EXTRA_ID, post.id)
        intent.putExtra(PostDetailActivity.EXTRA_POST, post)
        context.startActivity(intent)
    }

    fun onCommentButtonClick(id: Int?) {
        val intent = Intent(context, PostCommentActivity::class.java)
        intent.putExtra(PostDetailActivity.EXTRA_ID, id ?: 0)

        context.startActivity(intent)
    }

    fun onChatCardClick(id: Int) {
        val intent = Intent(context, DetailChatActivity::class.java)
        intent.putExtra(DetailChatActivity.EXTRA_ID, id)

        context.startActivity(intent)
    }

    fun onNewChatClick() {
        val intent = Intent(context, DetailChatActivity::class.java)
        context.startActivity(intent)
    }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        "${stringResource(R.string.hi)} $profileName,",
                        style = MaterialTheme.typography.h6.copy(
                            color = Color(0xFF454545),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "${stringResource(R.string.good_to_see_you_again)}, ${stringResource(R.string.help_today_question)}",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color(0xFF454545),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left
                        ),
                    )
                }
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF07AFFF),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 12.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_idea),
                        contentDescription = stringResource(R.string.tips),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        stringResource(R.string.tips_chat),
                        style = MaterialTheme.typography.body2.copy(
                            color = Color(0xFF454545),
                            textAlign = TextAlign.Center
                        ),
                    )
                }
                PrimaryButton(onClick = { onNewChatClick() }, {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24_white),
                        contentDescription = stringResource(R.string.tips),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(R.string.new_chat),
                        color = Color.White,
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                })
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    SubHeaderHome(
                        title = stringResource(R.string.history),
                        onClick = onClickSeeChat
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(getHeightChat())
                    ) {
                        if (listChat.isNotEmpty()) {
                            items(listChat, key = { it.id }) { data ->
                                HistoryChatCardHome(
                                    latestChat = if (data.chats.isNotEmpty()) data.chats[0].response else "-",
                                    date = if (data.chats.isNotEmpty()) data.chats[0].createdAt.withDateFormatFromISO() else data.createdAt.withDateFormatFromISO(),
                                    onClickSeeChat = { onChatCardClick(data.id) }
                                )
                            }
                        } else {
                            item {
                                ErrorText(text = stringResource(R.string.no_history_data))
                            }
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    ),
                ) {
                    SubHeaderHome(
                        title = stringResource(R.string.most_liked_post),
                        onClick = onClickSeePost
                    )
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(getHeightPost())
                    ) {
                        if (listPost.isNotEmpty()) {
                            items(listPost, key = { it.id }) { data ->
                                PostCard(
                                    username = data.posterFullName,
                                    profPic = data.profilePicOfUser,
                                    title = data.title,
                                    date = data.createdAt.withDateFormatFromISO(),
                                    likeCount = data.likeCount,
                                    dontLikeCount = data.dontLikeCount,
                                    commentCount = data.commentCount,
                                    isUserLike = data.isUserLike,
                                    isUserDontLike = data.isUserDontLike,
                                    onClickCard = {
                                        onPostCardClick(data)
                                    },
                                    onClickComment = { onCommentButtonClick(data.id) }
                                )
                            }
                        } else {
                            item {
                                ErrorText(text = stringResource(R.string.no_most_liked_post_data))
                            }
                        }
                    }
                }
            }
        }
    }
}