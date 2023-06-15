package com.travelah.travelahapp.data.remote.models.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LikePostResponse(
    val data: LikePost,
    val message: String,
    val status: Boolean
) : Parcelable

@Parcelize
data class LikePost(
	val createdAt: String,
	val likeType: String,
	val id: Int,
	val postId: Int,
	val userId: Int,
	val updatedAt: String
) : Parcelable
