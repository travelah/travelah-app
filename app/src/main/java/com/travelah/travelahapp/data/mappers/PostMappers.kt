package com.travelah.travelahapp.data.mappers

import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.remote.models.Post

fun PostEntity.toPost(): Post {
    return  Post(
        id = id,
        title = title,
        userId = userId,
        latitude = latitude,
        longitude = longitude,
        description = description,
        commentCount = commentCount,
        isUserLike = isUserLike,
        isUserDontLike = isUserDontLike,
        likeCount = likeCount,
        dontLikeCount = dontLikeCount,
        posterFullName = posterFullName,
        postPhotoName = postPhotoName,
        postPhotoPath = postPhotoPath,
        location = location,
        profilePicOfUser = profilePicOfUser,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
