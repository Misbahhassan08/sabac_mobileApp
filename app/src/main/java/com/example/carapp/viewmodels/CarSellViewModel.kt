package com.example.carapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.models.DateWithSlots
import com.example.carapp.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarSellViewModel @Inject constructor(private val repository: AppRepository): ViewModel() {

    val appointments: StateFlow<List<DateWithSlots>> = repository.appointmentSlots

    init {
        viewModelScope.launch {
            repository.getDateWithSlots()
        }
    }
}