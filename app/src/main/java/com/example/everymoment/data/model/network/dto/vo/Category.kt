package com.example.everymoment.data.model.network.dto.vo


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
)