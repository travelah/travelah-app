package com.travelah.travelahapp.data.remote.pager

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.local.entity.PostRemoteKeysEntity
import com.travelah.travelahapp.data.local.room.TravelahDatabase
import com.travelah.travelahapp.data.remote.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val database: TravelahDatabase,
    private val apiService: ApiService,
    private val token: String,
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
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
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val responseData = apiService.getAllPost(token,  page, state.config.pageSize)
            val endOfPaginationReached = responseData.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.postRemoteKeysDao().deleteRemoteKeys()
                    database.postDao().deleteAllPost()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val data = responseData.data.map {
                    PostEntity(
                        id = it.id,
                        title = it.title,
                        userId = it.userId,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        description = it.description,
                        commentCount = it.commentCount,
                        isUserLike = it.isUserLike,
                        isUserDontLike = it.isUserDontLike,
                        likeCount = it.likeCount,
                        dontLikeCount = it.dontLikeCount,
                        posterFullName = it.posterFullName,
                        postPhotoName = it.postPhotoName,
                        postPhotoPath = it.postPhotoPath,
                        location = it.location,
                        profilePicOfUser = it.profilePicOfUser,
                        createdAt = it.createdAt,
                        updatedAt = it.updatedAt
                    )
                }

                val keys = responseData.data.map {
                    PostRemoteKeysEntity(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.postRemoteKeysDao().insertAll(keys)
                database.postDao().insert(data)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostEntity>): PostRemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.postRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PostEntity>): PostRemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.postRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, PostEntity>): PostRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.postRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}