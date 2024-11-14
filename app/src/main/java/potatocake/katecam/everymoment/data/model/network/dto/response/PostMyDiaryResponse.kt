package potatocake.katecam.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.Category

data class PostMyDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: MyDiaryDetailResponse,
    @SerializedName("message")
    val message: String
)

data class MyDiaryDetailResponse(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("content")
    val content: String?,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("emoji")
    val emoji: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("locationName")
    val locationName: String,
    @SerializedName("liked")
    var liked: Boolean
)