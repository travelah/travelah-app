package com.travelah.travelahapp.data.remote.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Post(
	@field:SerializedName("dontLikeCount")
	val dontLikeCount: Int,

	@field:SerializedName("isUserDontLike")
	val isUserDontLike: Boolean,

	@field:SerializedName("latitude")
	val latitude: Double? = null,

	@field:SerializedName("posterFullName")
	val posterFullName: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("likeCount")
	val likeCount: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("commentCount")
	val commentCount: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("profilePicOfUser")
	val profilePicOfUser: String? = null,

	@field:SerializedName("postPhotoPath")
	val postPhotoPath: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("postPhotoName")
	val postPhotoName: String? = null,

	@field:SerializedName("isUserLike")
	val isUserLike: Boolean? = null,

	@field:SerializedName("longitude")
	val longitude: Double? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
) : Parcelable
