package com.travelah.travelahapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChat(chat: List<ChatItem>)

    @Query("SELECT * FROM chat")
    fun getAllGroupChat(): PagingSource<Int, ChatItem>

    @Query("DELETE FROM chat")
    suspend fun deleteAll()
}