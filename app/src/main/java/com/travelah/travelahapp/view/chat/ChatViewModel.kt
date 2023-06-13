package com.travelah.travelahapp.view.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.data.remote.ChatRepository
import kotlinx.coroutines.flow.map

class ChatViewModel(
private val chatRepository: ChatRepository
) : ViewModel() {
    fun getRecentHistoryChat(token: String) = chatRepository.getHistoryChat(token)

    fun getGroupChatHistory(token: String): LiveData<PagingData<ChatEntity>> = chatRepository.getHistoryGroupChat(token).cachedIn(viewModelScope)

    fun deleteGroupChatById(token: String, id: Int) = chatRepository.deleteGroupChat(token, id)

    fun getAllChatInGroup(groupId:Int, token: String) =
        chatRepository.getAllChatInGroup(groupId, token).map {
            val postMap = mutableSetOf<Int>()
            it.filter { post ->
                if (postMap.contains(post.id)) {
                    false
                } else {
                    postMap.add(post.id)
                }
            }
        }.cachedIn(viewModelScope)
}