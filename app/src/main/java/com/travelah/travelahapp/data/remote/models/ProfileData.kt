package com.travelah.travelahapp.data.remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileData(
    val fullName: String,
    val photo: String,
    val aboutMe: String,
    val age: Int,
    val occupation: String,
    val location: String
): Parcelable