package com.travelah.travelahapp.view.register

import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.view.ViewModelFactory

class RegisterViewModel(
    private val repository: UserRepository
) : ViewModel() {
    fun postRegister(email: String, password: String, fullName: String) = repository.postRegister(email, password, fullName)
}