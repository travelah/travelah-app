package com.travelah.travelahapp.data.remote.models.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

@Parcelize
data class ChatDetailResponse(
    @field:SerializedName("data")
    val data: List<ChatItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Boolean
) : Parcelable {
    companion object {
        fun fromJson(json: JSONObject): ChatDetailResponse {
            val data = mutableListOf<ChatItem>()
            val dataArray = json.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val chatItem = ChatItem(
                    groupChatId = item.getInt("groupChatId"),
                    createdAt = item.getString("createdAt"),
                    question = item.getString("question"),
                    response = item.getString("response"),
                    bookmarked = item.getBoolean("bookmarked"),
                    id = item.getInt("id"),
                    userId = item.getInt("userId"),
                    user = User(
                        profilePicPath = item.getJSONObject("user").optString("profilePicPath"),
                        profilePicName = item.getJSONObject("user").optString("profilePicName")
                    ),
                    chatType = item.getInt("chatType"),
                    updatedAt = item.getString("updatedAt"),
                    altIntent1 = item.getString("altIntent1"),
                    altIntent2 = item.getString("altIntent2")
                )
                data.add(chatItem)
            }

            val message = json.getString("message")
            val status = json.getBoolean("status")

            return ChatDetailResponse(data, message, status)
        }
    }
}

@Parcelize
data class User(
    @field:SerializedName("profilePicPath")
    val profilePicPath: String? = null,

    @field:SerializedName("profilePicName")
    val profilePicName: String? = null
) : Parcelable

@Parcelize
data class ChatItem(
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

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("chatType")
    val chatType: Int,

    @field:SerializedName("altIntent1")
    val altIntent1: String? = null,

    @field:SerializedName("altIntent2")
    val altIntent2: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String,
) : Parcelable
