package com.example.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName

data class FriendsListResponse(
    val code: Int,
    val message: String,
    val info: FriendListInfo
)

data class FriendListInfo(
    val friends: List<Friends>,
    val next: Int
)

data class Friends(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?,

    @SerializedName("close")
    val close: Boolean
)
