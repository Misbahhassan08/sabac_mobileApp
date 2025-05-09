package com.example.myapplication.api

//import com.example.myapplication.models.TweetListItem
import com.example.carapp.models.DateWithSlots
import com.example.carapp.models.LoginRequest
import com.example.carapp.models.RegisterRequest
import com.example.carapp.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AppAPI {

//    @GET("/v3/b/67484e70ad19ca34f8d1ee9f?meta=false")
//    suspend fun getTweets(@Header("X-JSON-Path") category: String): Response<List<TweetListItem>>

    @GET("/categories")
    @Headers("X-JSON-Path: tweets..category")
    suspend fun getCategories(): Response<List<String>>

    @GET("/user")
    suspend fun getUser()

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<User>

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @POST("/submitSlots")
    suspend fun submitSlots(@Body dateWithSlotsList: List<DateWithSlots>): Response<Unit>

    @GET("/getSlots")
    suspend fun getAppointmentSlots() : Response<List<DateWithSlots>>

}
