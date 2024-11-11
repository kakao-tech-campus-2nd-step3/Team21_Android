package potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail


import com.google.gson.annotations.SerializedName

data class GetFriendDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: Post,
    @SerializedName("message")
    val message: String
)