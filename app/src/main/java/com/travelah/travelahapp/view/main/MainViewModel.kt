package com.travelah.travelahapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelah.travelahapp.data.remote.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    fun getToken(): LiveData<String> = repository.getToken()
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}