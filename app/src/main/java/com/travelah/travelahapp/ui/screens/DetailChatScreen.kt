package com.travelah.travelahapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.travelah.travelahapp.ui.components.contents.DetailChatContent
import com.travelah.travelahapp.ui.components.elements.AppBarChat

@Composable
fun DetailChatScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()).fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AppBarChat(fullName = "Zuhal 'Alimul Hadi")
        DetailChatContent(modifier = Modifier)
    }
}