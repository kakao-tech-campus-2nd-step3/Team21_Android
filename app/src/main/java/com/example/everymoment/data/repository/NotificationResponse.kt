package com.example.everymoment.data.repository

data class NotificationResponse(
    val code: Int,
    val message: String,
    val info: List<MyNotification>
)

data class MyNotification(
    val id: Int,
    val content: String,
    val type: String,
    val targetId: Int,
    val createAt: String,
    val read: Boolean
)
