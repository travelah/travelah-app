package com.travelah.travelahapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "chat")
data class ChatEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val latestChat: String,

    val latestChatDate: String
): Parcelable
