package com.travelah.travelahapp.ui.components.contents

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.travelah.travelahapp.R
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.ui.components.elements.BubbleChat
import com.travelah.travelahapp.utils.SocketHandler
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun DetailChatContent(
    token: String,
    id: Int,
    listChat: List<ChatItem>,
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val counter = remember { mutableStateOf(0) }

    fun handleInput(value: String) {
        input = value
    }

    fun handleSubmit() {
        val payload = JSONObject()
        payload.put("groupId", id)
        payload.put("question", input)
        payload.put("token", token)

        SocketHandler.getSocket().emit("createChatByGroup", payload)
        SocketHandler.getSocket().on(
            "chatRetrieved"
        ) { args ->
            val response = args[0] as JSONObject?

            if (response != null) {
                input = ""
                coroutineScope.launch {
                    listState.animateScrollToItem(index = listChat.size)
                }
            }
        }
    }

    SocketHandler.getSocket().on(
        "chatCreationError"
    ) { args ->
        val response = args[0] as JSONObject?

        if (response != null) {
            (context as? Activity)?.runOnUiThread {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_send_chat),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LaunchedEffect(key1 = counter) {
        coroutineScope.launch {
            listState.animateScrollToItem(index = listChat.size)
        }
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = modifier
                .fillMaxHeight(0.88f)
                .fillMaxWidth()
        ) {
            items(listChat, key = { it.id }) { chat ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    BubbleChat(chat, true)
                    BubbleChat(chat, false, {
                        handleInput(chat.altIntent1 ?: "")
                    }, {
                        handleInput(chat.altIntent2 ?: "")
                    })
                }
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