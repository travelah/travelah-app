package com.travelah.travelahapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "chat_remote_keys")
@Parcelize
data class ChatRemoteKeysEntity(
    @PrimaryKey val id: Int,
    val prevKey: Int?,
    val nextKey: Int?
): Parcelable
