package com.travelah.travelahapp.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.travelah.travelahapp.data.remote.models.Profile
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
            preferences[EMAIL_KEY] = profile.data.email
            preferences[USER_ID_KEY] = profile.data.id
            preferences[NAME_KEY] = profile.data.fullName
            preferences[IS_SIGNED_BY_GOOGLE_KEY] = profile.data.isSignedByGoogle
        }
    }

    suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[USER_ID_KEY] = 0
            preferences[NAME_KEY] = ""
            preferences[IS_SIGNED_BY_GOOGLE_KEY] = false
        }
    }

    fun getProfileSetting(): Flow<Profile> {
        return dataStore.data.map { preferences ->
            Profile(
                email = preferences[EMAIL_KEY] ?: "",
                fullName = preferences[NAME_KEY]?: "",
                id = preferences[USER_ID_KEY] ?: 0,
                isSignedByGoogle = preferences[IS_SIGNED_BY_GOOGLE_KEY] ?: false
            )
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val NAME_KEY = stringPreferencesKey("fullName")
        private val IS_SIGNED_BY_GOOGLE_KEY = booleanPreferencesKey("isSignedByGoogle")
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