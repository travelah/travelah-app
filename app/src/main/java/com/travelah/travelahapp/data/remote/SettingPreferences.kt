package com.travelah.travelahapp.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.travelah.travelahapp.data.remote.models.ProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getTokenSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun saveTokenSetting(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveProfile(profile: ProfileResponse) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = profile.email
            preferences[USER_ID_KEY] = profile.id
        }
    }

    suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[USER_ID_KEY] = 0
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = intPreferencesKey("userId")

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}