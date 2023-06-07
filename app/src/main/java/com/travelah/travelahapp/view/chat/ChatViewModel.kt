package com.travelah.travelahapp.view.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.travelah.travelahapp.data.database.ChatItem
import com.travelah.travelahapp.data.remote.ChatRepository

class ChatViewModel(
private val chatRepository: ChatRepository
) : ViewModel() {
    fun getRecentHistoryChat(token: String) = chatRepository.getHistoryChat(token)

    fun getGroupChatHistory(token: String): LiveData<PagingData<ChatItem>> =  chatRepository.getHistoryGroupChat(token).cachedIn(viewModelScope)

    fun deleteGroupChatById(token: String, id: Int) = chatRepository.deleteGroupChat(token, id)
}