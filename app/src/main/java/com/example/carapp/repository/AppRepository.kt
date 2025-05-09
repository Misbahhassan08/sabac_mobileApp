package com.example.carapp.repository

import android.util.Log
import com.example.carapp.models.DateWithSlots
import com.example.carapp.models.User
import com.example.myapplication.api.AppAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject

class AppRepository @Inject constructor(private val appAPI: AppAPI) {
    private val _appointmentSlots = MutableStateFlow<List<DateWithSlots>>(emptyList())
    val appointmentSlots: StateFlow<List<DateWithSlots>> = _appointmentSlots

    suspend fun getDateWithSlots() {
        val response = appAPI.getAppointmentSlots()
        if (response.isSuccessful && response.body() != null ) {
            _appointmentSlots.emit(response.body()!!)
        }
    }
    suspend fun submitDateWithSlots(dateWithSlotsList: List<DateWithSlots>): Response<Unit> {
        return appAPI.submitSlots(dateWithSlotsList)
    }

}