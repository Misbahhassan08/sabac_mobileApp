package com.example.carapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carapp.models.DateWithSlots
import com.example.carapp.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InspectionViewModel @Inject constructor( private val repository: AppRepository) : ViewModel() {

    private val _response = MutableStateFlow<Result<Unit>?>(null)
    val response: StateFlow<Result<Unit>?> = _response

    fun submitSlots(dateWithSlotsList: List<DateWithSlots>) {
        viewModelScope.launch {
            try {
                val result = repository.submitDateWithSlots(dateWithSlotsList)
                if (result.isSuccessful) {
                    _response.emit(Result.success(Unit))
                } else {
                    _response.emit(Result.failure(Exception("Error: ${result.code()}")))
                }
            } catch (e: Exception) {
                _response.emit(Result.failure(e))
            }
        }
    }
}