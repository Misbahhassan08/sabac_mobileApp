package com.example.carapp.screens

import kotlinx.serialization.Serializable

@Serializable
data class CarData(
    val car_name: String,
    val company: String,
    val year: String,
    val engine_size: String,
    val milage: String,
    val option_type: String,
    val paint_condition: String,
    val specs: String,
    val photos: List<String>
)