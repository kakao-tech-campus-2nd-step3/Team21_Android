package potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary


import com.google.gson.annotations.SerializedName

data class PatchEditedDiaryRequest(
    @SerializedName("address")
    val address: String = "",
    @SerializedName("categories")
    val categories: List<Category> = emptyList(),
    @SerializedName("content")
    val content: String = "",
    @SerializedName("contentDelete")
    val contentDelete: Boolean = true,
    @SerializedName("deleteAllCategories")
    val deleteAllCategories: Boolean = true,
    @SerializedName("emoji")
    val emoji: String? = "",
    @SerializedName("emojiDelete")
    val emojiDelete: Boolean = true,
    @SerializedName("locationName")
    val locationName: String = ""
)