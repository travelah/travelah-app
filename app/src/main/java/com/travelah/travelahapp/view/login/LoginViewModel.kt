package com.travelah.travelahapp.view.login

import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.TravelahRepository

class LoginViewModel(
    private val repository: TravelahRepository,
) : ViewModel() {
    fun postLogin(email: String, password: String) = repository.postLogin(email, password)
}