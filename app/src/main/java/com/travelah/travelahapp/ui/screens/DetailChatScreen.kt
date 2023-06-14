package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.ui.components.contents.DetailChatContent
import com.travelah.travelahapp.ui.components.elements.AppBarChat

@Composable
fun DetailChatScreen(
    token: String,
    id: Int,
    chatResponse: ChatDetailResponse,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier.height(configuration.screenHeightDp.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBarChat(fullName = "Chat Detail", onBackClick = onBackClick)
        DetailChatContent(
            token = token,
            id = id,
            listChat = chatResponse.data,
            modifier = Modifier
        )
    }
}