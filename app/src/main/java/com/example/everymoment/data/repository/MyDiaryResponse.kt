package com.example.everymoment.data.repository

import com.google.gson.annotations.SerializedName

data class DiaryResponse(
    val code: Int,
    val message: String,
    val info: DiaryInfo
)

data class DiaryInfo(
    val diaries: List<Diary>,
    val next: Int
)

data class Diary(
    @SerializedName("id")
    val id: Int,

    @SerializedName("locationName")
    val locationName: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("emoji")
    val emoji: String,

    @SerializedName("thumbnailResponse")
    val thumbnailResponse: ThumbnailResponse?,

    @SerializedName("content")
    val content: String,

    @SerializedName("createAt")
    val createAt: String,

    @SerializedName("public")
    val public: Boolean,

    @SerializedName("bookmark")
    val bookmark: Boolean
)

data class ThumbnailResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("imageUrl")
    val imageUrl: String
)
