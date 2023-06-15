package com.travelah.travelahapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.travelah.travelahapp.data.local.entity.ChatEntity
import com.travelah.travelahapp.data.local.entity.ChatRemoteKeysEntity
import com.travelah.travelahapp.data.local.entity.PostEntity
import com.travelah.travelahapp.data.local.entity.PostRemoteKeysEntity

@Database(
    entities = [PostEntity::class, PostRemoteKeysEntity::class, ChatEntity::class, ChatRemoteKeysEntity::class],
    version = 5,
    exportSchema = false
)
abstract class TravelahDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeysDao(): PostRemoteKeysDao
    abstract fun chatDao(): ChatDao
    abstract fun chatRemoteKeysDao(): ChatRemoteKeysDao


    companion object {
        @Volatile
        private var instance: TravelahDatabase? = null
        fun getInstance(context: Context): TravelahDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    TravelahDatabase::class.java, "Travelah.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}