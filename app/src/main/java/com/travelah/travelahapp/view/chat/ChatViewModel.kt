package com.travelah.travelahapp.view.chat

import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.ChatRepository

class ChatViewModel(
private val chatRepository: ChatRepository
) : ViewModel() {
    fun getRecentHistoryChat(token: String) = chatRepository.getHistoryChat(token)
}