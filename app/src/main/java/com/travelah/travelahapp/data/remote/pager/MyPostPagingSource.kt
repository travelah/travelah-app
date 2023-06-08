package com.travelah.travelahapp.data.remote.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.remote.retrofit.ApiService

class MyPostPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, PostEntity>() {
    override val jumpingSupported: Boolean = true

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostEntity> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllMyPost(token, position, params.loadSize)
            val convertedPost = responseData.data.map {
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
            LoadResult.Page(
                data = convertedPost,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (convertedPost.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PostEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}