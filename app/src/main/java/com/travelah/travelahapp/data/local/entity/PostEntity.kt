package com.travelah.travelahapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "post")
@Parcelize
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val userId: Int,
    val latitude: Double,
    val posterFullName: String,
    val description: String,
    val commentCount: Int,
    val profilePicOfUser: String? = null,
    val postPhotoPath: String? = null,
    val location: String? = null,
    val postPhotoName: String? = null,
    val likeCount: Int,
    val isUserLike: Boolean,
    val dontLikeCount: Int,
    val isUserDontLike: Boolean,
    val longitude: Double,
    val createdAt: String,
    val updatedAt: String? = null
) : Parcelable