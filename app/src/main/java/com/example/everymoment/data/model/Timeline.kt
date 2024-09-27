package com.example.everymoment.data.model

data class Timeline(val time: String,
                    val buildingName: String,
                    val address: String,
                    val emoji: String?,
                    val isDetailedDiary: Boolean)