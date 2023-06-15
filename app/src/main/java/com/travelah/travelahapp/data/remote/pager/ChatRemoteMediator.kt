package com.travelah.travelahapp.data.remote.pager

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.data.local.entity.ChatRemoteKeysEntity
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.local.entity.PostRemoteKeysEntity
import com.travelah.travelahapp.data.local.room.TravelahDatabase
import com.travelah.travelahapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val database: TravelahDatabase,
    private val apiService: ApiService,
    private val token: String
): RemoteMediator<Int, ChatEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                Log.d("Append", remoteKeys?.id.toString())
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        Log.d("Check", page.toString())

        try {
            Log.d("Api call", page.toString())
            val responseData = apiService.getAllHistoryChat(token, page, state.config.pageSize)
            var endOfPaginationReached = responseData.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.chatRemoteKeysDao().deleteRemoteKeys()
                    database.chatDao().deleteAll()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val listGroupChat = responseData.data.map {
                    ChatEntity(it.chats[0].groupChatId, it.chats[0].response, it.chats[0].updatedAt)
                }

                val keys = responseData.data.map {
                    ChatRemoteKeysEntity(id = it.chats[0].groupChatId, prevKey = prevKey, nextKey = nextKey)
                }

                database.chatRemoteKeysDao().insertAll(keys)
                database.chatDao().insertGroupChat(listGroupChat)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ChatEntity>): ChatRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.chatRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ChatEntity>): ChatRemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.chatRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ChatEntity>): ChatRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.chatRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}