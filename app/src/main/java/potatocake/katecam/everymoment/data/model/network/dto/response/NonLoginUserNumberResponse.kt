package potatocake.katecam.everymoment.data.model.network.dto.response

data class NonLoginUserNumberResponse(
    val code: Int,
    val message: String,
    val info: Info
) {
    data class Info(
        val number: Int?,
        val token: String
    )
}