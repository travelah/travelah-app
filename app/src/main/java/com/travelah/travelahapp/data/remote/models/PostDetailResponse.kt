package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostDetailResponse(
	@field:SerializedName("data")
	val data: Post,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
): Parcelable
