package com.travelah.travelahapp.data.remote.retrofit

import androidx.room.Delete
import com.travelah.travelahapp.data.remote.models.*
import com.travelah.travelahapp.data.remote.models.body.RegisterBody
import com.travelah.travelahapp.data.remote.models.body.LoginBody
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginBody
    ): LoginResponse

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterBody
    ): RegisterResponse

    @GET("users/profile")
    suspend fun profile(
        @Header("Authorization") authorization: String,
    ): ProfileResponse

    @GET("posts/most-liked")
    suspend fun getAllMostLikedPost(
        @Header("Authorization") authorization: String,
    ): MostLikedPostResponse

    @GET("chats")
    suspend fun getAllHistoryChat(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 3
    ): HistoryChatResponse

    @DELETE("chats/group/{id}")
    suspend fun deleteGroupChat(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
    ): DeleteGroupChatResponse
}