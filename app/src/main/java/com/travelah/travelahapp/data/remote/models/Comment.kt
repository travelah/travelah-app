package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("postId")
	val postId: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("user")
	val user: UserComment,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
): Parcelable

@Parcelize
data class UserComment(
	@field:SerializedName("profilePicPath")
	val profilePicPath: String? = null,

	@field:SerializedName("fullName")
	val fullName: String
): Parcelable
