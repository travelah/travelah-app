package com.travelah.travelahapp.view.login

import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.UserRepository

class LoginViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    fun postLogin(email: String, password: String) = repository.postLogin(email, password)
}