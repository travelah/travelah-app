package com.travelah.travelahapp.data.remote.models.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.travelah.travelahapp.data.remote.models.Chat

@Parcelize
data class HistoryChatResponse(
    @field:SerializedName("data")
	val data: List<HistoryChat>,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class HistoryChat(
    @field:SerializedName("createdAt")
	val createdAt: String,

    @field:SerializedName("chats")
	val chats: List<Chat>,

    @field:SerializedName("id")
	val id: Int,

    @field:SerializedName("userId")
	val userId: Int,

    @field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
