package com.example.everymoment.data.model.network.dto.request.postEditDiary


import com.google.gson.annotations.SerializedName

data class PostEditDiaryRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("content")
    val content: String,
    @SerializedName("emoji")
    val emoji: String?,
    @SerializedName("locationName")
    val locationName: String
)