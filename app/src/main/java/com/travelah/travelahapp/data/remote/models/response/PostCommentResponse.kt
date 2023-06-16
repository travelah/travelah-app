package com.travelah.travelahapp.data.remote.models.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.travelah.travelahapp.data.remote.models.Comment

@Parcelize
data class PostCommentResponse(
    @field:SerializedName("data")
	val data: List<Comment>,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class User(
	@field:SerializedName("profilePicPath")
	val profilePicPath: String? = null,

	@field:SerializedName("fullName")
	val fullName: String
) : Parcelable
