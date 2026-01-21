package com.example.beer.ui.popups

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

private const val TAG = "ImageFunctions"

/**
 * Copies an image from an external URI (e.g., gallery) to the app's internal storage.
 * This ensures the app maintains access to the image even if the external source
 * becomes unavailable or the permission expires.
 */
fun copyImageToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        val resultUri = Uri.fromFile(file).toString()
        Log.i(TAG, "Successfully copied image to internal storage: $resultUri")
        resultUri
    } catch (e: Exception) {
        Log.e(TAG, "Error copying image to internal storage", e)
        null
    }
}

/**
 * Saves a Bitmap (e.g., from a camera capture) into the app's internal storage directory.
 * Uses JPEG compression at 90% quality to balance file size and visual clarity.
 */
fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String? {
    return try {
        val fileName = "beer_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        val resultUri = Uri.fromFile(file).toString()
        Log.i(TAG, "Successfully saved bitmap to internal storage: $resultUri")
        resultUri
    } catch (e: Exception) {
        Log.e(TAG, "Error saving bitmap to internal storage", e)
        null
    }
}