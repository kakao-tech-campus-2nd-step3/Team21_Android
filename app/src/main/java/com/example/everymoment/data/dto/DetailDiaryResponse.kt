package com.example.everymoment.data.dto

import com.google.gson.annotations.SerializedName

data class DetailDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: DetailDiary,
    @SerializedName("message")
    val message: String
)

data class DetailDiary(
    @SerializedName("address")
    val address: String,
    @SerializedName("bookmark")
    val bookmark: Boolean,
    @SerializedName("categories")
    val categories: List<DetailDiaryCategory>,
    @SerializedName("content")
    val content: String,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("emoji")
    val emoji: String,
    @SerializedName("file")
    val `file`: List<DetailDiaryFile>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("locationName")
    val locationName: String
)

data class DetailDiaryCategory(
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
)

data class DetailDiaryFile(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("order")
    val order: Int
)