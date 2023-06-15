package com.travelah.travelahapp.data.remote.retrofit

import com.travelah.travelahapp.data.remote.models.*
import com.travelah.travelahapp.data.remote.models.body.CommentPostBody
import com.travelah.travelahapp.data.remote.models.body.RegisterBody
import com.travelah.travelahapp.data.remote.models.body.LoginBody
import com.travelah.travelahapp.data.remote.models.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): AllPostResponse

    @GET("chats")
    suspend fun getAllHistoryChat(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int? = 1,
        @Query("take") take: Int? = 3
    ): HistoryChatResponse

    @DELETE("chats/group/{id}")
    suspend fun deleteGroupChat(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
    ): DeleteGroupChatResponse
    @GET("posts")
    suspend fun getAllPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int? = 1,
        @Query("take") take: Int? = 3
    ): AllPostResponse

    @GET("posts/mypost")
    suspend fun getAllMyPost(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int? = 1,
        @Query("take") take: Int? = 3
    ): AllPostResponse

    @POST("posts/like/{id}")
    suspend fun likeDislikePost(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Query("likeType") likeType: String,
    ): LikePostResponse

    @Multipart
    @PUT("users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part?,
        @Part("fullName") fullName: RequestBody?,
        @Part("age") age: RequestBody?,
        @Part("occupation") occupation: RequestBody?,
        @Part("location") location: RequestBody?,
        @Part("aboutMe") aboutMe: RequestBody?
    ): UpdateProfileResponse
    @POST("posts")
    suspend fun createPost(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part photo: MultipartBody.Part,
    ): CreatePostResponse

    @GET("posts/detail/{id}")
    suspend fun getPostDetail(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
    ): PostDetailResponse

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
    ): PostDetailResponse

    @POST("posts/comment/{id}")
    suspend fun createCommentPost(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body body: CommentPostBody
    ): CreatePostCommentResponse

    @GET("posts/all-comments/{id}")
    suspend fun getAllCommentPost(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Query("page") page: Int? = 1,
        @Query("take") take: Int? = 3
    ): PostCommentResponse
}