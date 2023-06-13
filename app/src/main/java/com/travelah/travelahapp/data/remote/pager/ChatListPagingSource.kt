package com.travelah.travelahapp.data.remote.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.travelah.travelahapp.data.remote.models.response.ChatDetailResponse
import com.travelah.travelahapp.data.remote.models.response.ChatItem
import com.travelah.travelahapp.utils.SocketHandler
import org.json.JSONObject
import kotlinx.coroutines.channels.Channel

class ChatListPagingSource(private val groupId: Int, private val token: String) :
    PagingSource<Int, ChatItem>() {
    override val jumpingSupported: Boolean = true

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val payload = JSONObject()
            payload.put("groupId", groupId)
            payload.put("page", position)
            payload.put("take", params.loadSize)
            payload.put("token", token)

            val channel = Channel<ChatDetailResponse>() // Create a suspendable channel for receiving the WebSocket response

            // Listen for the response from the server
            SocketHandler.getSocket().on("chatRetrieved"
            ) { args ->
                val response = args[0] as JSONObject
                val responseData = ChatDetailResponse.fromJson(response)
                channel.trySend(responseData) // Send the response data through the channel
            }

            SocketHandler.getSocket().emit("getAllChatFromGroupChat", payload)

//            delay(8000)
            val responseData = channel.receive() // Receive the WebSocket response from the channel

            LoadResult.Page(
                data = responseData.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ChatItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}