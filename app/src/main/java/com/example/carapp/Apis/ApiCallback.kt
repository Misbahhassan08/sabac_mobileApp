package com.example.carapp.Apis

interface ApiCallback {
    fun onSuccess(response: String)
    fun onFailure(error: String)
}