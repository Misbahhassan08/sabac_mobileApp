package com.example.carapp.models

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/*
class InspectionModel : ViewModel() {
    private val _submittedReports = MutableStateFlow<Set<String>>(emptySet())
    val submittedReports: StateFlow<Set<String>> = _submittedReports.asStateFlow()

//    fun isReportSubmitted(id: String): Boolean {
//        val result = _submittedReports.value.contains(id)
//        Log.d("InspectionModel", "isReportSubmitted($id) = $result")
//        return result
//    }

    fun markReportSubmitted(id: String) {
        Log.d("InspectionModel", "Adding $id to submittedReports")
        _submittedReports.value = _submittedReports.value + id
        Log.d("InspectionModel", "Updated submittedReports: ${_submittedReports.value}")
    }
}
*/


class InspectionModel : ViewModel() {
    private val _submittedReports = MutableStateFlow(setOf<String>())
    val submittedReports: StateFlow<Set<String>> = _submittedReports.asStateFlow()

    init {
        Log.d("InspectionModel", "ViewModel Created: $this")
    }

    fun markReportSubmitted(id: String) {
        Log.d("InspectionModel", "Adding $id to submittedReports, ViewModel: $this")
        _submittedReports.value = _submittedReports.value + id
        Log.d("InspectionModel", "Updated submittedReports: ${_submittedReports.value}")
    }
}
