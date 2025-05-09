package com.example.carapp.models

sealed class AuthState {
    object Idle: AuthState()
    object Loading: AuthState()
    data class Success(val email: String, val role: String): AuthState()
    data class Error(val message: String): AuthState()
}