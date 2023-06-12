package com.travelah.travelahapp.view.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.UserRepository

class SplashScreenViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    init {
        _token.value = userRepository.getToken().toString()
    }
}