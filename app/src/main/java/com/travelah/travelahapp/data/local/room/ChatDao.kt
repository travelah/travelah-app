package com.travelah.travelahapp.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.travelah.travelahapp.data.local.entity.ChatEntity

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupChat(chat: List<ChatEntity>)

    @Query("SELECT * from chat")
    fun getAllGroupChat(): PagingSource<Int, ChatEntity>

    @Query("DELETE FROM chat")
    suspend fun deleteAll()
}