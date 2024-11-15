package potatocake.katecam.everymoment

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.*
import potatocake.katecam.everymoment.data.repository.impl.FriendDiaryRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class FriendDiaryRepositoryTest {
    @Mock
    private lateinit var apiService: PotatoCakeApiService

    @Mock
    private lateinit var mockCall: Call<DiaryResponse>

    private lateinit var repository: FriendDiaryRepositoryImpl
    private val token = "Bearer test_token"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = FriendDiaryRepositoryImpl(apiService, token)
    }

    @Test
    fun getFriendDiariesSuccessfully() {
        val friendId = 1
        val mockDiary = Diary(
            id = 1,
            locationName = "강원대 미술관",
            address = "춘천시 강원대학로 1",
            emoji = "🎨",
            thumbnailResponse = ThumbnailResponse(1, "test_url"),
            content = "친구의 일기 내용",
            createAt = "2024-01-01T12:00:00",
            public = true,
            bookmark = false
        )
        val mockResponse = DiaryResponse(
            code = 200,
            message = "Success",
            info = DiaryInfo(
                diaries = listOf(mockDiary),
                next = 0
            )
        )

        `when`(apiService.getFriendDiaries(token, friendId)).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<DiaryResponse>
            callback.onResponse(mockCall, Response.success(mockResponse))
            null
        }.`when`(mockCall).enqueue(any())

        var result: DiaryResponse? = null
        var success = false
        repository.getFriendDiaries(friendId) { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getFriendDiaries(token, friendId)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun getAllFriendDiariesByDate() {
        val date = "2024-01-01"
        val mockDiaries = listOf(
            Diary(
                id = 1,
                locationName = "강원대 정문",
                address = "춘천시 강원대학로 1",
                emoji = "☺️",
                thumbnailResponse = ThumbnailResponse(1, "test_url_1"),
                content = "첫 번째 친구의 일기",
                createAt = "2024-01-01T12:00:00",
                public = true,
                bookmark = false
            ),
            Diary(
                id = 2,
                locationName = "강원대 도서관",
                address = "춘천시 강원대학로 1",
                emoji = "☺️",
                thumbnailResponse = ThumbnailResponse(2, "test_url_2"),
                content = "두 번째 친구의 일기",
                createAt = "2024-01-01T14:00:00",
                public = true,
                bookmark = false
            )
        )
        val mockResponse = DiaryResponse(
            code = 200,
            message = "Success",
            info = DiaryInfo(
                diaries = mockDiaries,
                next = 0
            )
        )

        `when`(apiService.getTotalFriendDiaries(token, date)).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<DiaryResponse>
            callback.onResponse(mockCall, Response.success(mockResponse))
            null
        }.`when`(mockCall).enqueue(any())

        var result: DiaryResponse? = null
        var success = false
        repository.getTotalFriendDiaries(date) { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getTotalFriendDiaries(token, date)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
        assertEquals(2, result?.info?.diaries?.size)
    }
}