package com.travelah.travelahapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.travelah.travelahapp.data.remote.TravelahRepository

class MainViewModel(
    private val repository: TravelahRepository,
) : ViewModel() {
    fun getToken(): LiveData<String> = repository.getToken()
}