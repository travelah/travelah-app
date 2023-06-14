package com.travelah.travelahapp.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.data.remote.models.ProfileData
import com.travelah.travelahapp.data.remote.models.ProfileResponse
import com.travelah.travelahapp.data.remote.models.UpdatedProfile
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

    suspend fun updateProfile(p: UpdatedProfile) {
        dataStore.edit {  preferences ->
            preferences[NAME_KEY] = p.fullName.toString()
            preferences[PHOTO_KEY] = "${p.profilePicPath}/${p.profilePicName}"
            preferences[ABOUT_ME_KEY] = p.aboutMe.toString()
            preferences[AGE_KEY] = p.age as Int
            preferences[OCCUPATION_KEY] = p.occupation.toString()
            preferences[LOCATION_KEY] = p.location.toString()
        }
    }

    fun getUpdatedProfile(): Flow<ProfileData> {
        return dataStore.data.map { preferences ->
            ProfileData(
                fullName = preferences[NAME_KEY] ?: "",
                photo = preferences[PHOTO_KEY] ?: "",
                aboutMe = preferences[ABOUT_ME_KEY] ?: "",
                age = preferences[AGE_KEY] ?: 0,
                occupation = preferences[OCCUPATION_KEY] ?: "",
                location = preferences[LOCATION_KEY] ?: ""
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
        private val PHOTO_KEY = stringPreferencesKey("photo")
        private val AGE_KEY = intPreferencesKey("age")
        private val OCCUPATION_KEY = stringPreferencesKey("occupation")
        private val LOCATION_KEY = stringPreferencesKey("location")
        private val ABOUT_ME_KEY = stringPreferencesKey("aboutMe")

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}