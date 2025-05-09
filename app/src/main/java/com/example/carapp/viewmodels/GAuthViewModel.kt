package com.example.carapp.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.carapp.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GAuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {
    fun getGoogleSignInIntent(context: Context): Intent {
        return repository.getGSignInClient(context)
    }

    fun handleGoogleSignIn(data: Intent?): GoogleSignInAccount? {
        return repository.handleGSignIn(data)
    }
}