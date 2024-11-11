package potatocake.katecam.everymoment.data.model.network.dto.response

data class CoordinatesResponse(
    val code: Int,
    val message: String,
    val info: LocationInfo
)

data class LocationInfo(
    val latitude: Double,
    val longitude: Double
)