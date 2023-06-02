package com.travelah.travelahapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelah.travelahapp.data.remote.UserRepository
import com.travelah.travelahapp.data.remote.models.Profile
import com.travelah.travelahapp.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    private val _tokenState: MutableStateFlow<UiState<String>> = MutableStateFlow(UiState.Loading)
    val tokenState: StateFlow<UiState<String>>
        get() = _tokenState

    init {
        getTokenFlow()
    }

    fun getToken(): LiveData<String> = repository.getToken()

    private fun getTokenFlow() = viewModelScope.launch {
        repository.getTokenFlow().collect { token ->
            _tokenState.value = UiState.Success(token)
        }
    }

    fun getProfile(): LiveData<Profile> = repository.getProfile()
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}