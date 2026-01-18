package com.example.beer.ui.popups

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        // Create a unique filename
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        // Copy the data from the URI to your local file
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        // Return the absolute path or a file URI
        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String? {
    return try {
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        Uri.fromFile(file).toString()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}