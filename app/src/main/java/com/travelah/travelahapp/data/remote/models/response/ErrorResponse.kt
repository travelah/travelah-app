package com.travelah.travelahapp.data.remote.models.response

data class ErrorResponse (
    val message: String = "",
    val stack: String = "",
    val status: Boolean = false
)