package com.example.everymoment.data.model.network.dto.vo


import com.google.gson.annotations.SerializedName

data class File(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("order")
    val order: Int
)