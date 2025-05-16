package com.example.carapp.repository


import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.Flow
import com.google.firebase.auth.AuthResult


interface Auth_Repository_G{

    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>

//    fun googleSignIn(credential: AuthCredential): Flow<Resource<AuthResult>>
}