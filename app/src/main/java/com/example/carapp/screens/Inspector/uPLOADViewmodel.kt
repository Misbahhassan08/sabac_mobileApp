package com.example.carapp.screens.Inspector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import java.io.File


/*
class UploadViewModel : ViewModel() {
    private val repository = ImageUploadRepository()

    fun uploadImages(imagePaths: List<String>, onComplete: (List<String>) -> Unit) {
        viewModelScope.launch {
            val results = mutableListOf<String>()
            imagePaths.forEach { path ->
                val file = File(path)
                if (file.exists()) {
                    val result = repository.uploadImage(file)
                    results.add("${file.name}: $result")
                } else {
                    results.add("${file.name}: File not found")
                }
            }
            onComplete(results)
        }
    }
}*/

class UploadViewModel : ViewModel() {
    private val repository = ImageUploadRepository()

    fun uploadImages(imagePaths: List<String>, onComplete: (List<Pair<String, JsonObject?>>) -> Unit) {
        viewModelScope.launch {
            val results = mutableListOf<Pair<String, JsonObject?>>()
            imagePaths.forEach { path ->
                val file = File(path)
                if (file.exists()) {
                    val result = repository.uploadImage(file)
                    results.add(result)
                } else {
                    val errorResponse = JsonObject().apply {
                        addProperty("error", "File not found")
                        addProperty("filename", path)
                        addProperty("success", false)
                    }
                    results.add(Pair("File not found: $path", errorResponse))
                }
            }
            onComplete(results)
        }
    }
}


class UploadViewModel2 : ViewModel() {
    private val repository = ImageUploadRepository()

    fun uploadImages(imagePaths: List<String>, onComplete: (List<Pair<String, JsonObject?>>) -> Unit) {
        viewModelScope.launch {
            val results = mutableListOf<Pair<String, JsonObject?>>()
            imagePaths.forEach { path ->
                val file = File(path)
                if (file.exists()) {
                    val result = repository.uploadImage(file)
                    results.add(result)
                } else {
                    val errorResponse = JsonObject().apply {
                        addProperty("error", "File not found")
                        addProperty("filename", path)
                        addProperty("success", false)
                    }
                    results.add(Pair("File not found: $path", errorResponse))
                }
            }
            onComplete(results)
        }
    }
}
