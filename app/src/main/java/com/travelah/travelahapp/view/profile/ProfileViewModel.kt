package com.travelah.travelahapp.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.data.remote.models.ProfileData

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getProfile(): LiveData<ProfileData> = userRepository.getUpdatedProfile()
}