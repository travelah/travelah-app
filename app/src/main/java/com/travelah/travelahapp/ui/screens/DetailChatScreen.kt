package com.travelah.travelahapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.ui.components.contents.DetailChatContent
import com.travelah.travelahapp.ui.components.contents.PostContent
import com.travelah.travelahapp.ui.components.elements.AppBarChat

@Composable
fun DetailChatScreen(listChat: LazyPagingItems<ChatItem>, token: String, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    LaunchedEffect(key1 = listChat.loadState) {
        if (listChat.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error: " + (listChat.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    if (listChat.loadState.refresh is LoadState.Loading) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF07AFFF)
            )
        }
    } else {
        Column(
            modifier = modifier.height(configuration.screenHeightDp.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AppBarChat(fullName = "Chat Detail")
            DetailChatContent(listChat = listChat, token=token, modifier = Modifier)
        }
    }
}