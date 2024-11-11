package potatocake.katecam.everymoment.data.model.network.dto.response.getLikeCnt


import com.google.gson.annotations.SerializedName

data class GetLikeCountResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val likeCount: LikeCount,
    @SerializedName("message")
    val message: String
)