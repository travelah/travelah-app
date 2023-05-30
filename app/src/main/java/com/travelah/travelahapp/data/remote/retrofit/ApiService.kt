package com.travelah.travelahapp.data.remote.retrofit

import com.travelah.travelahapp.data.remote.models.AuthResponse
import com.travelah.travelahapp.data.remote.models.body.LoginBody
import com.travelah.travelahapp.data.remote.models.ProfileResponse
import com.travelah.travelahapp.data.remote.models.RegisterResponse
import com.travelah.travelahapp.data.remote.models.body.RegisterBody
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginBody
    ): AuthResponse

    @POST("auth/register")
    suspend fun register(
        @Body body: RegisterBody
    ): RegisterResponse

    @GET("users/profile")
    suspend fun profile(
        @Header("Authorization") authorization: String,
    ): ProfileResponse
}