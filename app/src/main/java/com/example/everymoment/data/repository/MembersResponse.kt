package com.example.everymoment.data.repository

data class MemberResponse(
    val info: MemberInfo
)

data class MemberInfo(
    val members: List<Member>
)
data class Member(
    val id: Int,
    val nickname: String,
    val profileImage: String
)