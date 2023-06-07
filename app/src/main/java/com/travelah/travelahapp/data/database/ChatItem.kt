package com.travelah.travelahapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "chat")
data class ChatItem(
    @PrimaryKey
    val id: Int,

    val latestChat: String,

    val latestChatDate: String
): Parcelable
