package com.travelah.travelahapp.data.remote.models

import okhttp3.RequestBody

data class Location(
    val latitude: RequestBody,
    val longitude:RequestBody
)