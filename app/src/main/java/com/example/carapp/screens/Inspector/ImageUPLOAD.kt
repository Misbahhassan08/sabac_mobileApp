package com.example.carapp.screens.Inspector

import android.util.Log
import com.example.carapp.Apis.TestApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.FileWriter
import java.io.IOException

/*
class ImageUploadRepository {
    private val client = OkHttpClient()
    private val uploadUrl = "https://mexemai.com/bucket/upload"

    suspend fun uploadImage(file: File): String {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "image",
                        file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .build()

                val request = Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "Empty response"

                Log.d("UploadResponse", "Upload successful: $responseBody")
                responseBody
            } catch (e: Exception) {
                Log.e("UploadError", "Failed to upload image", e)
                "Upload failed: ${e.message}"
            }
        }
    }
}*/

 */


class ImageUploadRepository {
    private val client = OkHttpClient()
    private val uploadUrl = TestApi.Bucket_Upload
    private val gson = Gson()

    suspend fun uploadImage(file: File): Pair<String, JsonObject?> {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "image",
                        file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .build()

                val request = Request.Builder()
                    .url(uploadUrl)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: "{}"

                val jsonResponse = try {
                    JsonParser().parse(responseBody).asJsonObject
                } catch (e: Exception) {
                    JsonObject().apply {
                        addProperty("error", "Invalid JSON response")
                        addProperty("raw_response", responseBody)
                    }
                }

                jsonResponse.addProperty("filename", file.name)
                jsonResponse.addProperty("upload_time", System.currentTimeMillis().toString())
                jsonResponse.addProperty("success", response.isSuccessful)

                saveResponseToJson(file, jsonResponse)

                Log.d("UploadResponse", "Upload response: ${jsonResponse}")

                //Pair("Upload successful", jsonResponse)
                Pair("Upload failed: ", jsonResponse)

            } catch (e: Exception) {
                Log.e("UploadError", "Failed to upload image", e)
                val errorResponse = JsonObject().apply {
                    addProperty("error", e.message)
                    addProperty("filename", file.name)
                    addProperty("success", false)
                }
                saveResponseToJson(file, errorResponse)
                Pair("Upload failed: ${e.message}", errorResponse)
            }
        }
    }
    private fun saveResponseToJson(originalFile: File, jsonResponse: JsonObject) {
        try {
            val responsesDir = File(originalFile.parentFile, "responses")
            if (!responsesDir.exists()) {
                responsesDir.mkdirs()
            }
            val jsonFile = File(responsesDir, "${originalFile.nameWithoutExtension}_response.json")

            FileWriter(jsonFile).use { writer ->
                gson.toJson(jsonResponse, writer)
            }
            Log.d("JsonSave", "Response saved to: ${jsonFile.absolutePath}")
        } catch (e: IOException) {
            Log.e("JsonSaveError", "Failed to save JSON response", e)
        }
    }
}