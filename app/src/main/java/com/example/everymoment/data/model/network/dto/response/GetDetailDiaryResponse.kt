package com.example.everymoment.data.model.network.dto.response

import com.example.everymoment.data.model.network.dto.vo.DetailDiary
import com.google.gson.annotations.SerializedName

data class GetDetailDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: DetailDiary,
    @SerializedName("message")
    val message: String
)