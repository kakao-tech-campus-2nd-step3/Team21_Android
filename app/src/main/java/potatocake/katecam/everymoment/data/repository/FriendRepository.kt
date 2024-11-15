package potatocake.katecam.everymoment.data.repository

import potatocake.katecam.everymoment.data.model.network.dto.response.FriendRequestListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.FriendsListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.MemberResponse

interface FriendRepository {
    fun getFriendsList(
        callback: (Boolean, FriendsListResponse?) -> Unit
    )

    fun deleteFriend(
        friendId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun getFriendRequestList(
        callback: (Boolean, FriendRequestListResponse?) -> Unit
    )

    fun acceptFriendRequest(
        requestId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun rejectFriendRequest(
        requestId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun getMembers(
        callback: (Boolean, MemberResponse?) -> Unit
    )

    fun sendFriendRequest(
        memberId: Int,
        callback: (Boolean, MemberResponse?) -> Unit
    )

    fun getFriendsListWithPage(key: Int, callback: (Boolean, FriendsListResponse?) -> Unit)
}