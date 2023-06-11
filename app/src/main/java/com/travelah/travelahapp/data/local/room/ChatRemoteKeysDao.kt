package com.travelah.travelahapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.travelah.travelahapp.data.local.entity.ChatRemoteKeysEntity

@Dao
interface ChatRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ChatRemoteKeysEntity>)

    @Query("SELECT * FROM chat_remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: Int): ChatRemoteKeysEntity?

    @Query("DELETE FROM chat_remote_keys")
    suspend fun deleteRemoteKeys()
}