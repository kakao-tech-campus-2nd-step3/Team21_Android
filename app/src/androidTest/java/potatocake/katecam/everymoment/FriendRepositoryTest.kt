package potatocake.katecam.everymoment

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.*
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class FriendRepositoryTest {
    @Mock
    private lateinit var apiService: PotatoCakeApiService

    @Mock
    private lateinit var mockFriendsListCall: Call<FriendsListResponse>

    @Mock
    private lateinit var mockServerCall: Call<ServerResponse>

    @Mock
    private lateinit var mockFriendRequestListCall: Call<FriendRequestListResponse>

    @Mock
    private lateinit var mockMemberResponseCall: Call<MemberResponse>

    private lateinit var repository: FriendRepositoryImpl
    private val token = "Bearer test_token"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = FriendRepositoryImpl(apiService, token)
    }

    @Test
    fun getFriendsListSuccessfully() {
        val mockFriends = Friends(
            id = 1,
            nickname = "testUser",
            profileImageUrl = "test_url",
            close = false
        )
        val mockResponse = FriendsListResponse(
            code = 200,
            message = "Success",
            info = FriendListInfo(
                friends = listOf(mockFriends),
                next = 0
            )
        )

        `when`(apiService.getFriendsList(token)).thenReturn(mockFriendsListCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<FriendsListResponse>
            callback.onResponse(mockFriendsListCall, Response.success(mockResponse))
            null
        }.`when`(mockFriendsListCall).enqueue(any())

        var result: FriendsListResponse? = null
        var success = false
        repository.getFriendsList { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getFriendsList(token)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun deleteFriendSuccessfully() {
        val friendId = 1
        val mockResponse = ServerResponse(200, "Friend deleted successfully")

        `when`(apiService.deleteFriend(token, friendId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        var result: String? = null
        var success = false
        repository.deleteFriend(friendId) { isSuccess, message ->
            success = isSuccess
            result = message
        }

        verify(apiService).deleteFriend(token, friendId)
        assertTrue(success)
        assertNotNull(result)
    }

    @Test
    fun getFriendRequestListSuccessfully() {
        val mockRequest = FriendRequests(
            id = 1,
            senderId = 2,
            nickname = "requestUser",
            profileImageUrl = "test_url"
        )
        val mockResponse = FriendRequestListResponse(
            code = 200,
            message = "Success",
            info = FriendRequestListInfo(
                friendRequests = listOf(mockRequest),
                next = 0
            )
        )

        `when`(apiService.getFriendRequestList(token)).thenReturn(mockFriendRequestListCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<FriendRequestListResponse>
            callback.onResponse(mockFriendRequestListCall, Response.success(mockResponse))
            null
        }.`when`(mockFriendRequestListCall).enqueue(any())

        var result: FriendRequestListResponse? = null
        var success = false
        repository.getFriendRequestList { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getFriendRequestList(token)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun acceptFriendRequestSuccessfully() {
        val requestId = 1
        val mockResponse = ServerResponse(200, "Friend request accepted")

        `when`(apiService.acceptFriendRequest(token, requestId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        var result: String? = null
        var success = false
        repository.acceptFriendRequest(requestId) { isSuccess, message ->
            success = isSuccess
            result = message
        }

        verify(apiService).acceptFriendRequest(token, requestId)
        assertTrue(success)
        assertNotNull(result)
    }

    @Test
    fun rejectFriendRequestSuccessfully() {
        val requestId = 1
        val mockResponse = ServerResponse(200, "Friend request rejected")

        `when`(apiService.rejectFriendRequest(token, requestId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        var result: String? = null
        var success = false
        repository.rejectFriendRequest(requestId) { isSuccess, message ->
            success = isSuccess
            result = message
        }

        verify(apiService).rejectFriendRequest(token, requestId)
        assertTrue(success)
        assertNotNull(result)
    }

    @Test
    fun getMembersSuccessfully() {
        val mockMember = Member(
            id = 1,
            nickname = "testMember",
            profileImageUrl = "test_url",
            friendRequestStatus = "NONE"
        )
        val mockResponse = MemberResponse(
            info = MemberInfo(
                members = listOf(mockMember)
            )
        )

        `when`(apiService.getMembers(token)).thenReturn(mockMemberResponseCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<MemberResponse>
            callback.onResponse(mockMemberResponseCall, Response.success(mockResponse))
            null
        }.`when`(mockMemberResponseCall).enqueue(any())

        var result: MemberResponse? = null
        var success = false
        repository.getMembers { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getMembers(token)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun sendFriendRequestSuccessfully() {
        val memberId = 1
        val mockMember = Member(
            id = 1,
            nickname = "testMember",
            profileImageUrl = "test_url",
            friendRequestStatus = "PENDING"
        )
        val mockResponse = MemberResponse(
            info = MemberInfo(
                members = listOf(mockMember)
            )
        )

        `when`(apiService.sendFriendRequest(token, memberId)).thenReturn(mockMemberResponseCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<MemberResponse>
            callback.onResponse(mockMemberResponseCall, Response.success(mockResponse))
            null
        }.`when`(mockMemberResponseCall).enqueue(any())

        var result: MemberResponse? = null
        var success = false
        repository.sendFriendRequest(memberId) { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).sendFriendRequest(token, memberId)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }
}