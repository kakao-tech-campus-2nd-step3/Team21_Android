package potatocake.katecam.everymoment.data.model.network.dto.response.getComments


import com.google.gson.annotations.SerializedName

data class CommentFriend(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String
)