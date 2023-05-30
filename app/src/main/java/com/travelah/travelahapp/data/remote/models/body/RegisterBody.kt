package com.travelah.travelahapp.data.remote.models.body

data class RegisterBody (
    val email: String,
    val password: String,
    val fullName: String
)