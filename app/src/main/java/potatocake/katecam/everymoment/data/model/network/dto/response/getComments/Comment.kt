package potatocake.katecam.everymoment.data.model.network.dto.response.getComments


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("commentFriendResponse")
    val commentFriend: CommentFriend,
    @SerializedName("content")
    val content: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("id")
    val id: Int
)