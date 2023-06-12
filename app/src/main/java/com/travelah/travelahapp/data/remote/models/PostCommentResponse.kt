package com.travelah.travelahapp.data.remote.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

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
