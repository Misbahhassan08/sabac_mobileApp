package com.example.carapp.repository

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.carapp.models.AuthState
import com.example.carapp.models.LoginRequest
import com.example.carapp.models.RegisterRequest
import com.example.myapplication.api.AppAPI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val appAPI: AppAPI /*, @ApplicationContext private val context: Context*/) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _loginState = MutableStateFlow<String?>(null)
    val loginState: StateFlow<String?>
        get() = _loginState

    private val _signupState = MutableStateFlow<String?>(null)
    val signupState: StateFlow<String?>
        get() = _signupState

    private val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestProfile()
        .requestEmail()
        .requestIdToken("1022448994816-gms2k8qt24jpabbdpkf9hdana6tc9ss3.apps.googleusercontent.com")
        .build()

//    private val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)

    suspend fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val response = appAPI.login(LoginRequest(email, password))
            if(response.isSuccessful && response.body() != null) {
                Log.i("loginRESPONSE", "response received: ${response.body()}")
                _authState.value = AuthState.Success(response.body()!!.email, response.body()!!.role)
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Login Failed")
        }
    }

    suspend fun signup(name: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            val response = appAPI.register(RegisterRequest(name, email, password))
            if(response.isSuccessful && response.body() != null) {
                Log.i("loginRESPONSE", "response received: ${response.body()}")
            }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.message ?: "Signup Failed")
        }
    }

    fun getGSignInClient(context: Context): Intent {
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
        return googleSignInClient.signInIntent
    }

    fun handleGSignIn(data: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        return try {
            task.result
        } catch (e: Exception) {
            null
        }
    }
}