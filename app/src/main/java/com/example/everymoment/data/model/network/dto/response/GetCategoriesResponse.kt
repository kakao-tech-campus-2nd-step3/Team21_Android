package com.example.everymoment.data.model.network.dto.response

import com.example.everymoment.data.model.network.dto.vo.Category
import com.google.gson.annotations.SerializedName

data class GetCategoriesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val categories: List<Category>,
    @SerializedName("message")
    val message: String
)