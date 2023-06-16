package com.travelah.travelahapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.google.gson.Gson
import com.travelah.travelahapp.data.Result
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.data.local.room.TravelahDatabase
import com.travelah.travelahapp.data.remote.models.response.ErrorResponse
import com.travelah.travelahapp.data.remote.models.response.HistoryChat
import com.travelah.travelahapp.data.remote.pager.ChatRemoteMediator
import com.travelah.travelahapp.data.remote.retrofit.ApiService
import com.travelah.travelahapp.utils.wrapEspressoIdlingResource
import retrofit2.HttpException

class ChatRepository private constructor(
    private val travelahDatabase: TravelahDatabase,
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

    @OptIn(ExperimentalPagingApi::class)
    fun getHistoryGroupChat(token: String): LiveData<PagingData<ChatEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = ChatRemoteMediator(travelahDatabase, apiService, token),
            pagingSourceFactory = {
                travelahDatabase.chatDao().getAllGroupChat()
            }
        ).liveData
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
            database: TravelahDatabase,
            apiService: ApiService,
        ): ChatRepository =
            instance ?: synchronized(this) {
                instance ?: ChatRepository(database, apiService)
            }.also { instance = it }
    }
}