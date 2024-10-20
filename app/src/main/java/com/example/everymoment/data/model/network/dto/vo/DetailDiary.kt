package com.example.everymoment.data.model.network.dto.vo


import com.google.gson.annotations.SerializedName

data class DetailDiary(
    @SerializedName("address")
    val address: String,
    @SerializedName("bookmark")
    val bookmark: Boolean,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("content")
    val content: String,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("emoji")
    val emoji: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("locationName")
    val locationName: String
)