package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("likeCount")
    val likeCount: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("userId")
    val userId: Int,

    @field:SerializedName("updatedAt")
    val updatedAt: String
) : Parcelable
