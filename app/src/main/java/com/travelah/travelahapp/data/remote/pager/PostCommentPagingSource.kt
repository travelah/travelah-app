package com.travelah.travelahapp.data.remote.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.travelah.travelahapp.data.remote.models.Comment
import com.travelah.travelahapp.data.remote.retrofit.ApiService

class PostCommentPagingSource (private val apiService: ApiService, private val token: String, private val id: Int) :
    PagingSource<Int, Comment>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllCommentPost("Bearer $token", id, position, params.loadSize)
            val convertedPost = responseData.data

            LoadResult.Page(
                data = convertedPost,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (convertedPost.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}