package potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail


import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("content")
    val content: String?,
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("emoji")
    val emoji: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("likeCount")
    val likeCount: LikeCount,
    @SerializedName("locationName")
    val locationName: String,
    @SerializedName("liked")
    val liked: Boolean
)