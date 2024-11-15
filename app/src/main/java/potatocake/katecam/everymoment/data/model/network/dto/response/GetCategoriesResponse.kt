package potatocake.katecam.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName
import potatocake.katecam.everymoment.data.model.network.dto.vo.Category

data class GetCategoriesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val categories: List<Category>,
    @SerializedName("message")
    val message: String
)