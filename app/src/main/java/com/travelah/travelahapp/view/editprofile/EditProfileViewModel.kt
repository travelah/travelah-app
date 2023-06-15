package com.travelah.travelahapp.view.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.data.remote.models.ProfileData
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getProfile(): LiveData<ProfileData> = userRepository.getUpdatedProfile()

    fun updateProfile(
        token: String,
        photo: MultipartBody.Part?,
        fullName: RequestBody?,
        age: RequestBody?,
        occupation: RequestBody?,
        location: RequestBody?,
        aboutMe: RequestBody?
    ): LiveData<String> = userRepository.updateProfile(token, photo, fullName, age, occupation, location, aboutMe)
}