package com.travelah.travelahapp.data.remote.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Places(
	@field:SerializedName("lng")
	val lng: Double,

	@field:SerializedName("place")
	val place: String,

	@field:SerializedName("lat")
	val lat: Double
) : Parcelable
