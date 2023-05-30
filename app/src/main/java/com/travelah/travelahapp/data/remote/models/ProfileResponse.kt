package com.travelah.travelahapp.data.remote.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ProfileResponse(
	@field:SerializedName("data")
	val data: Profile,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
) : Parcelable

@Parcelize
data class Profile(
	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("isSignedByGoogle")
	val isSignedByGoogle: Boolean,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
