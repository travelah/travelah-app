package com.travelah.travelahapp.ui.components.contents
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.ui.components.elements.BubbleChat
import com.travelah.travelahapp.utils.SocketHandler
import org.json.JSONObject

@Composable
fun DetailChatContent(listChat: LazyPagingItems<ChatItem>, token: String, modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    val snackbar = remember { SnackbarHostState() }

    val chatItems = remember { mutableStateListOf<ChatItem>() } // Store chat items in a mutable list

    val onNewDataReceived: (List<ChatItem>) -> Unit = { newData ->
        chatItems.clear()
        chatItems.addAll(newData)
    }

    fun handleSubmit() {
        val payload = JSONObject()

        val currentPage = listChat.loadState.append as? LoadState.NotLoading
            ?: listChat.loadState.refresh as? LoadState.NotLoading

        val currentPageNumber = currentPage?.endOfPaginationReached?.let {
            if (it) listChat.itemCount else listChat.itemCount - 1
        } ?: 0

        payload.put("groupId", 3)
        payload.put("question", input)
        payload.put("userId", 1)
        payload.put("token", token)
        payload.put("page", (currentPageNumber / 5) + 1)
        payload.put("take", 5)

        SocketHandler.getSocket().emit("createChatByGroup", payload)
        listChat.refresh()
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier
                .height(540.dp)
                .fillMaxWidth()
        ) {
            items(listChat, key = { it.id }) { chat ->
                if (chat != null) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        BubbleChat(chat, true)
                        BubbleChat(chat, false)
                    }
                }
            }
            when (val append = listChat.loadState.append) {
                LoadState.Loading -> item {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFF07AFFF)
                        )
                    }
                }
                is LoadState.Error -> item {
                    LaunchedEffect(key1 = append) {
                        snackbar.showSnackbar(append.error.message ?: "")
                    }
                }
                else -> Unit
            }
        }

        val boxModifier = Modifier
            .padding(top = 20.dp)
            .background(color = Color(0xFFA0D7FB))
            .padding(horizontal = 20.dp, vertical = 8.dp)

        val (boxRef) = createRefs()

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
                trailingIcon = {
                    IconButton(
                        onClick = { handleSubmit() },
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