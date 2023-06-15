package com.travelah.travelahapp.data.remote.models.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.travelah.travelahapp.data.remote.models.Post

@Parcelize
data class AllPostResponse(
    @field:SerializedName("data")
	val data: List<Post>,

    @field:SerializedName("message")
	val message: String,

    @field:SerializedName("status")
	val status: Boolean
) : Parcelable

