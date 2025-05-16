package com.example.carapp.di

import android.content.Context
import com.example.carapp.repository.Auth_Repository_G
import com.example.carapp.repository.Auth_Repository_GImpl
//import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.firebase.FirebaseApp


/*
@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
//    fun provideFirebaseAuth() = FirebaseAuth.getInstance()
    fun provideFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth):Auth_Repository_G{
        return Auth_Repository_GImpl(firebaseAuth)
    }

}*/


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }



    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): Auth_Repository_G {
        return Auth_Repository_GImpl(firebaseAuth)
    }
}
