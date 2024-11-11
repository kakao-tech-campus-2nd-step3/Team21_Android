package potatocake.katecam.everymoment.data.model.network.dto.response.getComments


import com.google.gson.annotations.SerializedName

data class GetCommentsResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val commentList: CommentList,
    @SerializedName("message")
    val message: String
)