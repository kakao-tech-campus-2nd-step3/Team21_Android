package potatocake.katecam.everymoment.data.model.network.dto.response.getComments


import com.google.gson.annotations.SerializedName

data class CommentList(
    @SerializedName("comments")
    val comments: List<Comment>,
    @SerializedName("next")
    val next: Int?
)