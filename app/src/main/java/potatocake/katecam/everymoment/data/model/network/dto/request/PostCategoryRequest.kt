package potatocake.katecam.everymoment.data.model.network.dto.request


import com.google.gson.annotations.SerializedName

data class PostCategoryRequest(
    @SerializedName("categoryName")
    val categoryName: String
)