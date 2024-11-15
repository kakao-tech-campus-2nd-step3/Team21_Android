package potatocake.katecam.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName
import potatocake.katecam.everymoment.data.model.network.dto.vo.DetailDiary

data class GetDetailDiaryResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: DetailDiary,
    @SerializedName("message")
    val message: String
)