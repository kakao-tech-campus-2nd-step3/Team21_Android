package com.example.everymoment.data.repository

import com.google.gson.annotations.SerializedName

data class MemberResponse(
    val info: MemberInfo
)

data class MemberInfo(
    val members: List<Member>
)
data class Member(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,

    @SerializedName("friendRequestStatus")
    val friendRequestStatus: String
)