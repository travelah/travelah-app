package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.ErrorResponse
import com.travelah.travelahapp.data.remote.models.Post
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource
import retrofit2.HttpException

class PostRepository private constructor(
    private val apiService: ApiService,
) {
    private fun convertErrorResponse(stringRes: String?): ErrorResponse {
        return Gson().fromJson(stringRes, ErrorResponse::class.java)
    }

    fun getMostLikedPost(token: String) : LiveData<Result<List<Post>>> = liveData {
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

    companion object {
        @Volatile
        private var instance: PostRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): PostRepository =
            instance ?: synchronized(this) {
                instance ?: PostRepository(apiService)
            }.also { instance = it }
    }
}