package com.travelah.travelahapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.travelah.travelahapp.data.remote.PostRepository
import com.travelah.travelahapp.data.remote.SettingPreferences
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.data.remote.retrofit.RetrofitConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val apiService = RetrofitConfig.getApiService()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }

    fun providePostRepository(): PostRepository {
        val apiService = RetrofitConfig.getApiService()
        return PostRepository.getInstance(apiService)
    }
}