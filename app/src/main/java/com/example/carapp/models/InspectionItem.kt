package com.example.carapp.models

data class InspectionItem(
    val carName: String,
    val time: String,
    val location: String,
    val onClick: () -> Unit
)