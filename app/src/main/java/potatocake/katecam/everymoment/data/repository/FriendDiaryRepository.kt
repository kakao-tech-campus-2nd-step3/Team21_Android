package potatocake.katecam.everymoment.data.repository

import potatocake.katecam.everymoment.data.model.network.dto.response.DiaryResponse

interface FriendDiaryRepository {
    fun getFriendDiaries(
        friendId: Int,
        callback: (Boolean, DiaryResponse?) -> Unit
    )

    fun getFriendDiariesWithPage(
        friendId: Int,
        page: Int,
        callback: (Boolean, DiaryResponse?) -> Unit
    )

    fun getTotalFriendDiaries(
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    )

}