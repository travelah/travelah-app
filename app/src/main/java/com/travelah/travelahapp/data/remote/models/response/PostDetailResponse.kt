package com.travelah.travelahapp.data.remote.models.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.travelah.travelahapp.data.remote.models.Post
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
