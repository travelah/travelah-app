package com.travelah.travelahapp.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.remote.pager.PostRemoteMediator
import com.travelah.travelahapp.data.local.room.TravelahDatabase
import com.travelah.travelahapp.data.remote.models.*
import com.travelah.travelahapp.data.remote.pager.MyPostPagingSource
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PostRepository private constructor(
    private val apiService: ApiService,
    private val database: TravelahDatabase
) {
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
                jumpThreshold = 15
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