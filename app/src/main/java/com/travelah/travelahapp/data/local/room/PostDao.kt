package com.travelah.travelahapp.data.local.room;

import androidx.paging.PagingSource
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.travelah.travelahapp.data.local.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM post")
    fun getAllPost(): PagingSource<Int, PostEntity>

    @Query("DELETE FROM post")
    fun deleteAllPost()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(post: List<PostEntity>)
}
