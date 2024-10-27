package com.example.everymoment.data.model.network.dto.request.postEditDiary


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryId")
    val categoryId: Int
)