package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    @field:SerializedName("isSignedByGoogle")
    val isSignedByGoogle: Boolean,

    @field:SerializedName("fullName")
    val fullName: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("aboutMe")
    val aboutMe: String,

    @field:SerializedName("occupation")
    val occupation: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("profilePicPath")
    val profilePicPath: String,

    @field:SerializedName("profilePicName")
    val profilePicName: String,

    @field:SerializedName("age")
    val age: Int
) : Parcelable