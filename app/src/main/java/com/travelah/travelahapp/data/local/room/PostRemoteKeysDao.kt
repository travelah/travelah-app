package com.travelah.travelahapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.travelah.travelahapp.data.local.entity.PostRemoteKeysEntity

@Dao
interface PostRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PostRemoteKeysEntity>)

    @Query("SELECT * FROM post_remote_keys WHERE id = :id")
    suspend fun getRemoteKeysId(id: Int): PostRemoteKeysEntity?

    @Query("DELETE FROM post_remote_keys")
    suspend fun deleteRemoteKeys()
}