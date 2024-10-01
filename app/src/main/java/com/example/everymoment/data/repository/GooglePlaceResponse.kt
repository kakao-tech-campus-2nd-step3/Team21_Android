package com.example.everymoment.data.repository

data class GooglePlacesResponse(
    val results: List<Place>,
    val status: String
)

data class Place(
    val name: String,
    val geometry: Geometry,
    val vicinity: String
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)
