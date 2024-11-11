package potatocake.katecam.everymoment.data.model.network.dto.response


import potatocake.katecam.everymoment.data.model.network.dto.vo.File
import com.google.gson.annotations.SerializedName

data class GetFilesResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: List<File>,
    @SerializedName("message")
    val message: String
)