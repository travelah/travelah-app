package com.travelah.travelahapp.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Locale

fun String.withDateFormatFromISO(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US) // Input format for ISO string
    val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US) // Desired output format

    val date = inputFormat.parse(this) // Parse the ISO string

    return outputFormat.format(date) // Format the date to the desired format
}

private const val FILENAME_FORMAT = "dd-MMM-yyyy"
private const val MAXIMAL_SIZE = 1000000

fun rotateFile(file: File, angle: Float) {
    val matrix = Matrix()
    val bitmap = BitmapFactory.decodeFile(file.path)
    matrix.postRotate(angle)
    var result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
}