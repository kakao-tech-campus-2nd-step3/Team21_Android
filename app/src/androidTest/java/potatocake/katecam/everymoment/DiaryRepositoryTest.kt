package potatocake.katecam.everymoment

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.*
import potatocake.katecam.everymoment.data.repository.impl.DiaryRepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class DiaryRepositoryTest {
    @Mock
    private lateinit var apiService: PotatoCakeApiService

    @Mock
    private lateinit var mockCall: Call<DiaryResponse>

    @Mock
    private lateinit var mockServerCall: Call<ServerResponse>

    @Mock
    private lateinit var mockCoordinatesCall: Call<CoordinatesResponse>

    private lateinit var repository: DiaryRepositoryImpl
    private val token = "Bearer test_token"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = DiaryRepositoryImpl(apiService, token)
    }

    @Test
    fun getDiariesTest() {
        val date = "2024-01-01"
        val mockDiary = Diary(
            id = 1,
            locationName = "강원대 중앙도서관",
            address = "춘천시 강원대학로 1",
            emoji = "😊",
            thumbnailResponse = ThumbnailResponse(1, "test_url"),
            content = "Test content",
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

        `when`(apiService.getDiaries(token, date)).thenReturn(mockCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<DiaryResponse>
            callback.onResponse(mockCall, Response.success(mockResponse))
            null
        }.`when`(mockCall).enqueue(any())

        var result: DiaryResponse? = null
        var success = false
        repository.getDiaries(date) { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getDiaries(token, date)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
    }

    @Test
    fun updateBookmarkStatusTest() {
        val diaryId = 1
        val mockResponse = ServerResponse(200, "Bookmark updated successfully")

        `when`(apiService.updateBookmarkStatus(token, diaryId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        var result: String? = null
        var success = false
        repository.updateBookmarkStatus(diaryId) { isSuccess, message ->
            success = isSuccess
            result = message
        }

        verify(apiService).updateBookmarkStatus(token, diaryId)
        assertTrue(success)
        assertNotNull(result)
    }

    @Test
    fun deleteDiaryTest() {
        val diaryId = 1
        val mockResponse = ServerResponse(200, "Diary deleted successfully")

        `when`(apiService.deleteDiary(token, diaryId)).thenReturn(mockServerCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<ServerResponse>
            callback.onResponse(mockServerCall, Response.success(mockResponse))
            null
        }.`when`(mockServerCall).enqueue(any())

        var result: String? = null
        var success = false
        repository.deleteDiary(diaryId) { isSuccess, message ->
            success = isSuccess
            result = message
        }

        verify(apiService).deleteDiary(token, diaryId)
        assertTrue(success)
        assertNotNull(result)
    }

    @Test
    fun getDiaryLocationTest() {
        val diaryId = 1
        val mockLocationInfo = LocationInfo(37.8699, 127.7445)
        val mockResponse = CoordinatesResponse(200, "Location fetched successfully", mockLocationInfo)

        `when`(apiService.getDiaryLocation(token, diaryId)).thenReturn(mockCoordinatesCall)

        doAnswer { invocation ->
            val callback = invocation.arguments[0] as Callback<CoordinatesResponse>
            callback.onResponse(mockCoordinatesCall, Response.success(mockResponse))
            null
        }.`when`(mockCoordinatesCall).enqueue(any())

        var result: CoordinatesResponse? = null
        var success = false
        repository.getDiaryLocation(diaryId) { isSuccess, response ->
            success = isSuccess
            result = response
        }

        verify(apiService).getDiaryLocation(token, diaryId)
        assertTrue(success)
        assertNotNull(result)
        assertEquals(mockResponse, result)
        assertEquals(37.8699, result?.info?.latitude)
        assertEquals(127.7445, result?.info?.longitude)
    }
}