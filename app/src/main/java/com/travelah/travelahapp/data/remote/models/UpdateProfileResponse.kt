package com.travelah.travelahapp.data.remote.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

	@field:SerializedName("data")
	val data: UpdatedProfile? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UpdatedProfile(

	@field:SerializedName("isSignedByGoogle")
	val isSignedByGoogle: Boolean? = null,

	@field:SerializedName("occupation")
	val occupation: Any? = null,

	@field:SerializedName("fullName")
	val fullName: String? = null,

	@field:SerializedName("aboutMe")
	val aboutMe: Any? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("profilePicPath")
	val profilePicPath: String? = null,

	@field:SerializedName("location")
	val location: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("profilePicName")
	val profilePicName: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
