package com.example.carapp.assets

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object AssetHelper {
    private const val TEMP_ASSETS_FOLDER = "temp_assets"

    fun saveImageToTempAssets(context: Context, uri: Uri, fileName: String): String {
        // Create temp_assets directory if it doesn't exist
        val tempAssetsDir = File(context.filesDir, TEMP_ASSETS_FOLDER)
        if (!tempAssetsDir.exists()) {
            tempAssetsDir.mkdirs()
        }

        // Create the destination file
        val destinationFile = File(tempAssetsDir, fileName)

        // Copy the image
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(destinationFile).use { output ->
                input.copyTo(output)
            }
        }
        // Log when an image is saved
        Log.d("Renamed", "Image saved: $fileName")
        Log.d("Renamed", "Full path: ${destinationFile.absolutePath}")

        return destinationFile.absolutePath
    }

    fun getTempAssets(context: Context): List<File> {
        val tempAssetsDir = File(context.filesDir, TEMP_ASSETS_FOLDER)
        return if (tempAssetsDir.exists()) {
            tempAssetsDir.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun clearTempAssets(context: Context) {
        val tempAssetsDir = File(context.filesDir, TEMP_ASSETS_FOLDER)
        if (tempAssetsDir.exists()) {
            tempAssetsDir.listFiles()?.forEach { it.delete() }
            tempAssetsDir.delete()
        }
    }
}