package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(

    @field:SerializedName("groupChatId")
    val groupChatId: Int,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("question")
    val question: String,

    @field:SerializedName("response")
    val response: String,

    @field:SerializedName("bookmarked")
    val bookmarked: Boolean,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable