package potatocake.katecam.everymoment.data.model.network.dto.response

data class MyInformationResponse(
    val code: Int,
    val message: String,
    val info: MyInformation
)

data class MyInformation(
    val id: Int,
    val profileImageUrl: String,
    val nickname: String
)