package com.travelah.travelahapp.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.travelah.travelahapp.data.database.ChatDatabase
import com.travelah.travelahapp.data.database.ChatItem
import com.travelah.travelahapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val database: ChatDatabase,
    private val apiService: ApiService,
    private val token: String
): RemoteMediator<Int, ChatItem>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatItem>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        try {
            val responseData = apiService.getAllHistoryChat("Bearer $token", page, state.config.pageSize)
            var endOfPaginationData = responseData.data.isEmpty()
            val listGroupChat: List<ChatItem> = responseData.data.map {
                ChatItem(
                    it.chats[0].groupChatId, it.chats[0].response, it.chats[0].updatedAt
                )
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.chatDao().deleteAll()
                }
                database.chatDao().insertGroupChat(listGroupChat)
                Log.d("Success", "Success add to database")
            }
            return MediatorResult.Success(endOfPaginationReached == endOfPaginationData)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
            Log.d("Failed", "Failed to add to database")
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}