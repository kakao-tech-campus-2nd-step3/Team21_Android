package potatocake.katecam.everymoment

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.*
import potatocake.katecam.everymoment.data.repository.impl.NotificationRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class NotificationRepositoryTest {
    @Mock
    private lateinit var apiService: PotatoCakeApiService

    @Mock
    private lateinit var mockNotificationCall: Call<NotificationResponse>

    @Mock
    private lateinit var mockServerCall: Call<ServerResponse>

    private lateinit var repository: NotificationRepositoryImpl
    private val token = "Bearer test_token"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = NotificationRepositoryImpl(apiService, token)
    }

    @Test
    fun getNotificationListSuccessfully() {
        val mockNotifications = listOf(
            MyNotification(
                id = 1,
                content = "새로운 친구 요청이 있습니다",
                type = "FRIEND_REQUEST",
                targetId = 123,
                createAt = "2024-01-01T12:00:00",
                read = false
            ),
            MyNotification(
                id = 2,
                content = "게시물에 새로운 댓글이 있습니다",
                type = "NEW_COMMENT",
                targetId = 456,
                createAt = "2024-01-01T13:00:00",
                read = true
            )
        )

        val mockResponse = NotificationResponse(
            code = 200,
            message = "Success",
            info = mockNotifications
        )

        `when`(apiService.getNotifications(token)).thenReturn(mockNotificationCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<NotificationResponse>
            callback.onResponse(mockNotificationCall, Response.success(mockResponse))
            null
        }.`when`(mockNotificationCall).enqueue(any())

        var result: NotificationResponse? = null
        var success = false
        repository.getNotificationList { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getNotifications(token)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
        assertEquals(2, result?.info?.size)
    }

    @Test
    fun readNotificationSuccessfully() {
        val notificationId = 1
        val mockResponse = ServerResponse(200, "Notification marked as read")

        `when`(apiService.readNotification(token, notificationId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        repository.readNotification(notificationId)

        verify(apiService).readNotification(token, notificationId)
    }
}