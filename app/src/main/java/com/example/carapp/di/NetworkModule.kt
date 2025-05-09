package com.example.carapp.di

import android.content.Context
import com.example.myapplication.api.AppAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit{
        return Retrofit.Builder().baseUrl("http://192.168.10.11:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesAppAPI(retrofit: Retrofit): AppAPI {
        return retrofit.create(AppAPI::class.java)
    }

//    @Singleton
//    @Provides
//    fun provideContext(@ApplicationContext context: Context): Context {
//        return context
//    }
}