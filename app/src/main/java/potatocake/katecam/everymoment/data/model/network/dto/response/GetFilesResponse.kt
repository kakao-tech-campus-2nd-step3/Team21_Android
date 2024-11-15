package potatocake.katecam.everymoment.data.model.network.dto.response


import com.google.gson.annotations.SerializedName
import potatocake.katecam.everymoment.data.model.network.dto.vo.File

data class GetFilesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: List<File>,
    @SerializedName("message")
    val message: String
)