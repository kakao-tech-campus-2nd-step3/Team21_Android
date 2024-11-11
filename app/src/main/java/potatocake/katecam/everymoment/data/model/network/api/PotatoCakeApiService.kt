package potatocake.katecam.everymoment.data.model.network.api

import potatocake.katecam.everymoment.data.model.network.dto.request.EmojiRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.GetDetailDiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetCategoriesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetFilesResponse
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCategoryRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary.PatchEditedDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.CoordinatesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.DiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.FriendRequestListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.FriendsListResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.GetFriendDiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.MemberResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.MyInformationResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.NotificationResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.GetCommentsResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getLikeCnt.GetLikeCountResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PotatoCakeApiService {
    @GET("api/diaries/my")
    fun getDiaries(
        @Header("Authorization") token: String,
        @Query("date") date: String,
        @Query("key") key: Int=0
    ): Call<DiaryResponse>

    @PATCH("api/diaries/{diaryId}/bookmark")
    fun updateBookmarkStatus(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @PATCH("api/diaries/{diaryId}/privacy")
    fun updateShareStatus(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @DELETE("api/diaries/{diaryId}")
    fun deleteDiary(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @GET("/api/diaries/my/{diaryId}")
    fun getDiaryInDetail(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetDetailDiaryResponse>

    @POST("/api/categories")
    fun postCategory(
        @Header("Authorization") token: String,
        @Body categoryRequest: PostCategoryRequest
    ): Call<ServerResponse>

    @DELETE("/api/categories/{categoryId}")
    fun delCategory(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: Int
    ): Call<ServerResponse>

    @GET("/api/categories")
    fun getCategories(
        @Header("Authorization") token: String
    ): Call<GetCategoriesResponse>

    @GET("/api/diaries/{diaryId}/files")
    fun getFiles(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetFilesResponse>

    @Multipart
    @PUT("/api/diaries/{diaryId}/files")
    fun patchFiles(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Part files: List<MultipartBody.Part>
    ): Call<ServerResponse>

    @POST("api/members/{memberId}/friend-requests")
    fun sendFriendRequest(
        @Header("Authorization") token: String,
        @Path("memberId") memberId: Int
    ): Call<MemberResponse>

    @GET("api/friend-requests")
    fun getFriendRequestList(
        @Header("Authorization") token: String,
    ): Call<FriendRequestListResponse>

    @GET("api/friends/friends")
    fun getFriendsList(
        @Header("Authorization") token: String,
    ): Call<FriendsListResponse>

    @DELETE("api/friends/{friendId}")
    fun deleteFriend(
        @Header("Authorization") token: String,
        @Path("friendId") friendId: Int
    ): Call<ServerResponse>

    @POST("api/friend-requests/{requestId}/accept")
    fun acceptFriendRequest(
        @Header("Authorization") token: String,
        @Path("requestId") requestId: Int
    ): Call<ServerResponse>

    @DELETE("api/friend-requests/{requestId}/reject")
    fun rejectFriendRequest(
        @Header("Authorization") token: String,
        @Path("requestId") requestId: Int
    ): Call<ServerResponse>

    @GET("api/members?size=30")
    fun getMembers(
        @Header("Authorization") token: String,
    ): Call<MemberResponse>

    @GET("/api/friends/{friendId}/diaries")
    fun getFriendDiaries(
        @Header("Authorization") token: String,
        @Path("friendId") friendId: Int,
        @Query("key") key: Int=0
    ): Call<DiaryResponse>

    @GET("/api/diaries/friend")
    fun getTotalFriendDiaries(
        @Header("Authorization") token: String,
        @Query("date") date: String
    ): Call<DiaryResponse>

    @GET("/api/notifications")
    fun getNotifications(
        @Header("Authorization") token: String,
    ): Call<NotificationResponse>

    @PATCH("/api/notifications/{notificationId}")
    fun readNotification(
        @Header("Authorization") token: String,
        @Path("notificationId") notificationId: Int
    ): Call<ServerResponse>

    @GET("api/diaries/my")
    fun getSearchedDiaries(
        @Header("Authorization") token: String,
        @Query("keyword") keyword: String?,
        @Query("emoji") emoji: String?,
        @Query("category") category: String?,
        @Query("from") from: String?,
        @Query("until") until: String?,
        @Query("bookmark") bookmark: Boolean?
    ): Call<DiaryResponse>

    @PATCH("api/diaries/{diaryId}")
    fun updateEmojiStatus(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Body emojiRequest: EmojiRequest
    ): Call<ServerResponse>

    @PATCH("/api/diaries/{diaryId}")
    fun patchEditedDiary(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Body request: PatchEditedDiaryRequest
    ): Call<ServerResponse>

    @GET("/api/diaries/{diaryId}/location")
    fun getDiaryLocation(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<CoordinatesResponse>

    @GET("/api/members/me")
    fun getMyInfo(
        @Header("Authorization") token: String
    ): Call<MyInformationResponse>

    @Multipart
    @POST("/api/members")
    fun updateProfile(
        @Header("Authorization") token: String,
        @Part("nickname") nickname: RequestBody?,
        @Part profileImage: MultipartBody.Part?
    ): Call<ServerResponse>

    @GET("/api/members/anonymous-login")
    fun getAnonymousLogin(
    ): Call<NonLoginUserNumberResponse>

    @GET("/api/diaries/friend/{diaryId}")
    fun getFriendDiaryInDetail(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetFriendDiaryResponse>

    @POST("/api/diaries/{diaryId}/comments")
    fun postComment(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Body request: PostCommentRequest
    ): Call<ServerResponse>

    @GET("/api/diaries/{diaryId}/comments")
    fun getComments(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int,
        @Query("key") key: Int
    ): Call<GetCommentsResponse>

    @POST("/api/diaries/{diaryId}/likes")
    fun postLike(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<ServerResponse>

    @DELETE("/api/comments/{commentId}")
    fun delComment(
        @Header("Authorization") token: String,
        @Path("commentId") commentId: Int
    ): Call<ServerResponse>

    @GET("/api/diaries/{diaryId}/likes")
    fun getLikeCnt(
        @Header("Authorization") token: String,
        @Path("diaryId") diaryId: Int
    ): Call<GetLikeCountResponse>
}