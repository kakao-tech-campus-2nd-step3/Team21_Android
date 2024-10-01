package com.example.everymoment.data.repository

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
    val id: Int,
    val locationName: String,
    val address: String,
    val emoji: String,
    val thumbnailResponse: Thumbnail,
    val content: String,
    val createAt: String,
    val `public`: Boolean,
    val bookmark: Boolean
)

data class Thumbnail(
    val id: Int,
    val imageUrl: String
)