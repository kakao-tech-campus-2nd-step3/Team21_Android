package com.example.everymoment.data.model.network.dto.vo

import com.example.everymoment.data.model.entity.Emotions

data class FilterState(
    val selectedEmotions: String?,
    val isBookmarked: Boolean,
    val startDate: String?,
    val endDate: String?,
    val selectedCategories: String?
)
