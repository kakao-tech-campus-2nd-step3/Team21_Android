package com.example.everymoment.data.model.network.dto.vo

data class DiaryEntry(
    val locationPoint: LocationPoint,
    val locationName: String,
    val address: String
)

data class LocationPoint(
    val latitude: Double,
    val longitude: Double
)