package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.remote.models.ErrorResponse
import com.travelah.travelahapp.data.remote.models.HistoryChat
import com.travelah.travelahapp.data.remote.models.body.RegisterBody
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource
import retrofit2.HttpException

class ChatRepository private constructor(
    private val apiService: ApiService,
) {
    private fun convertErrorResponse(stringRes: String?): ErrorResponse {
        return Gson().fromJson(stringRes, ErrorResponse::class.java)
    }

    fun getHistoryChat(token: String) : LiveData<Result<List<HistoryChat>>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.getAllHistoryChat("Bearer $token", 1, 3)
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

    fun deleteGroupChat(token: String, id: Int) : LiveData<Result<String>> = liveData {
        emit(Result.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.deleteGroupChat(token, id)
                if (response.status) {
                    emit(Result.Success(response.message))
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
        private var instance: ChatRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): ChatRepository =
            instance ?: synchronized(this) {
                instance ?: ChatRepository(apiService)
            }.also { instance = it }
    }
}