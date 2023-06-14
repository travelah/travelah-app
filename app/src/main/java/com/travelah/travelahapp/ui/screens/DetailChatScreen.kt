package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.ui.components.contents.DetailChatContent
import com.travelah.travelahapp.ui.components.elements.AppBarChat

@Composable
fun DetailChatScreen(token: String, chatResponse: ChatDetailResponse, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = modifier.height(configuration.screenHeightDp.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBarChat(fullName = "Chat Detail")
        DetailChatContent(token = token, listChat =  chatResponse.data, modifier = Modifier)
    }
}