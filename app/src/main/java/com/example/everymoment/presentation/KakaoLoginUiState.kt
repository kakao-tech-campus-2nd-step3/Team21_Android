package com.example.everymoment.presentation

data class KakaoLoginUiState(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val userNickname: String? = null,
    val errorMessage: String? = null
)
