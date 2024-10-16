package com.example.everymoment.data.dto

import com.google.gson.annotations.SerializedName

data class GetCategoriesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val categories: List<Category>,
    @SerializedName("message")
    val message: String
)

data class Category(
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
)