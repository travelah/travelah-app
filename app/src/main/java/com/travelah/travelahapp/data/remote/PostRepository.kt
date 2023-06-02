package com.travelah.travelahapp.data.remote

import com.travelah.travelahapp.data.remote.retrofit.ApiService

class PostRepository private constructor(
    private val apiService: ApiService,
    private val pref: SettingPreferences
) {
    companion object {
        @Volatile
        private var instance: PostRepository? = null
        fun getInstance(
            apiService: ApiService,
            pref: SettingPreferences
        ): PostRepository =
            instance ?: synchronized(this) {
                instance ?: PostRepository(apiService, pref)
            }.also { instance = it }
    }
}