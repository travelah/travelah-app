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
            if (profile.data.profilePicName.isNullOrEmpty() && profile.data.profilePicPath.isNullOrEmpty()) {
                preferences[PHOTO_KEY] = "https://cdn-icons-png.flaticon.com/512/5556/5556468.png"
            } else {
                preferences[PHOTO_KEY] = "https://storage.googleapis.com/travelah-storage/${profile.data.profilePicPath}/${profile.data.profilePicName}"
            }
            preferences[PHOTO_PATH_KEY] = profile.data.profilePicPath
            preferences[PHOTO_NAME_KEY] = profile.data.profilePicName
            preferences[ABOUT_ME_KEY] = profile.data.aboutMe
            preferences[AGE_KEY] = profile.data.age
            preferences[OCCUPATION_KEY] = profile.data.occupation
            preferences[LOCATION_KEY] = profile.data.location
        }
    }

    suspend fun clearProfile() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[USER_ID_KEY] = 0
            preferences[NAME_KEY] = ""
            preferences[IS_SIGNED_BY_GOOGLE_KEY] = false
            preferences[PHOTO_KEY] = "https://cdn-icons-png.flaticon.com/512/5556/5556468.png"
            preferences[PHOTO_PATH_KEY] = ""
            preferences[PHOTO_NAME_KEY] = ""
            preferences[ABOUT_ME_KEY] = "I am a traveler"
            preferences[AGE_KEY] = 0
            preferences[OCCUPATION_KEY] = "-"
            preferences[LOCATION_KEY] = "-"
        }
    }

    fun getProfileSetting(): Flow<Profile> {
        return dataStore.data.map { preferences ->
            Profile(
                email = preferences[EMAIL_KEY] ?: "",
                fullName = preferences[NAME_KEY]?: "",
                id = preferences[USER_ID_KEY] ?: 0,
                isSignedByGoogle = preferences[IS_SIGNED_BY_GOOGLE_KEY] ?: false,
                aboutMe = preferences[ABOUT_ME_KEY] ?: "",
                occupation = preferences[OCCUPATION_KEY] ?: "",
                location = preferences[LOCATION_KEY] ?: "",
                profilePicName = preferences[PHOTO_NAME_KEY] ?: "",
                profilePicPath = preferences[PHOTO_PATH_KEY] ?: "",
                age = preferences[AGE_KEY] ?: 0
            )
        }
    }

    suspend fun updateProfile(p: UpdatedProfile) {
        dataStore.edit {  preferences ->
            preferences[NAME_KEY] = p.fullName.toString()
            if (p.profilePicName.isNullOrEmpty() && p.profilePicPath.isNullOrEmpty()) {
                preferences[PHOTO_KEY] = "https://cdn-icons-png.flaticon.com/512/5556/5556468.png"
            } else {
                preferences[PHOTO_KEY] = "https://storage.googleapis.com/travelah-storage/${p.profilePicPath}/${p.profilePicName}"
            }
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
                photo = preferences[PHOTO_KEY] ?: "https://cdn-icons-png.flaticon.com/512/5556/5556468.png",
                aboutMe = preferences[ABOUT_ME_KEY] ?: "I am a traveler",
                age = preferences[AGE_KEY] ?: 0,
                occupation = preferences[OCCUPATION_KEY] ?: "-",
                location = preferences[LOCATION_KEY] ?: "-"
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
        private val PHOTO_PATH_KEY = stringPreferencesKey("photoPath")
        private val PHOTO_NAME_KEY = stringPreferencesKey("photoName")
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