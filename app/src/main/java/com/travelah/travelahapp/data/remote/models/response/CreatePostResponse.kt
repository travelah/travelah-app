package com.travelah.travelahapp.data.remote.models.response

import com.google.gson.annotations.SerializedName

data class CreatePostResponse(
    @field:SerializedName("data")
	val data: CreatePost,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
)

data class CreatePost(
	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("postPhotoPath")
	val postPhotoPath: String,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("userId")
	val userId: Int,

	@field:SerializedName("postPhotoName")
	val postPhotoName: String,

	@field:SerializedName("longitude")
	val longitude: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
