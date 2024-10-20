package com.example.everymoment.data.model.network.dto.vo

data class KakaoLoginUiState(
    val isLoggedIn: Boolean = false,
    val userId: Long? = null,
    val userNickname: String? = null,
    val errorMessage: String? = null
)
