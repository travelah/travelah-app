package com.travelah.travelahapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun String.withDateFormatFromISO(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US) // Input format for ISO string
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US) // Desired output format

    val date = inputFormat.parse(this) // Parse the ISO string

    return outputFormat.format(date) // Format the date to the desired format
}