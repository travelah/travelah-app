package com.travelah.travelahapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.travelah.travelahapp.data.remote.SettingPreferences
import com.travelah.travelahapp.data.remote.TravelahRepository
import com.travelah.travelahapp.data.remote.retrofit.RetrofitConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context): TravelahRepository {
        val apiService = RetrofitConfig.getApiService()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return TravelahRepository.getInstance(apiService, pref)
    }
}