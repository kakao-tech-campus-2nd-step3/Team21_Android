package com.example.everymoment.data.repository

data class MemberResponse(
    val code: Int,
    val message: String,
    val info: Info
)

data class Info(
    val members: List<Member>,
    val next: Int
)

data class Member(
    val id: Int,
    val profileImageUrl: String,
    val nickname: String,
    val friendRequestStatus: String
)