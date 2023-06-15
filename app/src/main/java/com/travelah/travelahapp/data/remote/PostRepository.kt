package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.remote.pager.PostRemoteMediator
import com.travelah.travelahapp.data.local.room.TravelahDatabase
import com.travelah.travelahapp.data.remote.models.*
import com.travelah.travelahapp.data.remote.models.body.CommentPostBody
import com.travelah.travelahapp.data.remote.models.response.CreatePostCommentResponse
import com.travelah.travelahapp.data.remote.models.response.CreatePostResponse
import com.travelah.travelahapp.data.remote.models.response.ErrorResponse
import com.travelah.travelahapp.data.remote.models.response.LikePostResponse
import com.travelah.travelahapp.data.remote.pager.MyPostPagingSource
import com.travelah.travelahapp.data.remote.pager.PostCommentPagingSource
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class PostRepository private constructor(
    private val apiService: ApiService,
    private val database: TravelahDatabase
) {
    private val postDetail: MediatorLiveData<Result<Post>> = MediatorLiveData()

    private fun convertErrorResponse(stringRes: String?): ErrorResponse {
        return Gson().fromJson(stringRes, ErrorResponse::class.java)
    }

    fun getMostLikedPost(token: String): LiveData<Result<List<Post>>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.getAllMostLikedPost("Bearer $token")
                if (response.status) {
                    emit(Result.Success(response.data))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    fun getAllPost(token: String, isMyPost: Boolean = false): Flow<PagingData<PostEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
            ),
            remoteMediator = if (isMyPost) null else PostRemoteMediator(
                database,
                apiService,
                "Bearer $token"
            ),
            pagingSourceFactory = {
                if (isMyPost) MyPostPagingSource(
                    apiService,
                    "Bearer $token"
                ) else database.postDao()
                    .getAllPost()
            }
        ).flow
    }

    fun likeDislikePost(
        token: String,
        id: Int,
        isLike: Boolean
    ): Flow<Result<LikePostResponse>> = flow {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.likeDislikePost(
                    "Bearer $token",
                    id,
                    if (isLike) "LIKE" else "DONTLIKE"
                )
                if (response.status) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    fun createPost(
        photo: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        token: String,
        long: RequestBody,
        lat: RequestBody
    ): LiveData<Result<CreatePostResponse>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.createPost(
                    "Bearer $token",
                    title,
                    description,
                    lat,
                    long,
                    photo
                )
                if (response.status) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    fun createPostComment(
        description: String,
        id: Int,
        token: String,
    ): Flow<Result<CreatePostCommentResponse>> = flow {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.createCommentPost(
                    "Bearer $token",
                    id,
                    CommentPostBody(description),
                )
                if (response.status) {
                    emit(Result.Success(response))
                } else {
                    emit(Result.Error(response.message))
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                        val msg = jsonRes.message
                        emit(Result.Error(msg))
                    }
                    else -> {
                        emit(Result.Error(e.message.toString()))
                    }
                }
            }
        }
    }

    fun getLiveDataPostDetail(): LiveData<Result<Post>> {
        return postDetail
    }

    suspend fun getPostDetail(token: String, id: Int): LiveData<Result<Post>> {
        postDetail.value = Result.Loading

        try {
            val response = apiService.getPostDetail("Bearer $token", id)
            if (response.status) {
                postDetail.value = Result.Success(response.data)
            } else {
                postDetail.value = Result.Error(response.message)
            }
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val jsonRes = convertErrorResponse(e.response()?.errorBody()?.string())
                    val msg = jsonRes.message
                    postDetail.value = Result.Error(msg)
                }
                else -> {
                    postDetail.value = Result.Error(e.message.toString())
                }
            }
        }

        return postDetail
    }

    fun getAllCommentPost(token: String, id: Int): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 15
            ),
            pagingSourceFactory = {
                PostCommentPagingSource(apiService, token, id)
            }
        ).flow
    }

    companion object {
        @Volatile
        private var instance: PostRepository? = null
        fun getInstance(
            apiService: ApiService,
            database: TravelahDatabase
        ): PostRepository =
            instance ?: synchronized(this) {
                instance ?: PostRepository(apiService, database)
            }.also { instance = it }
    }
}