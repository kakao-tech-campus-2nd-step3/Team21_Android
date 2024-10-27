package com.example.everymoment.data.model.network.dto.request.postEditDiary


import com.google.gson.annotations.SerializedName

data class LocationPoint(
    @SerializedName("latitude")
    val latitude: Int,
    @SerializedName("longitude")
    val longitude: Int
)