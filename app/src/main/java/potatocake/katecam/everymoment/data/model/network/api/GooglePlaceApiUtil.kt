package potatocake.katecam.everymoment.data.model.network.api

import android.util.Log
import potatocake.katecam.everymoment.data.model.network.dto.response.GooglePlacesResponse

object GooglePlaceApiUtil {
    fun getPlaceNamesFromCoordinates(
        latitude: Double,
        longitude: Double,
        callback: (List<String>, List<String>) -> Unit
    ) {
        val apiKey = potatocake.katecam.everymoment.BuildConfig.API_KEY
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"

        val queryParams = mapOf(
            "location" to "$latitude,$longitude",
            "language" to "ko",
            "rankby" to "distance",
            "key" to apiKey
        )

        NetworkUtil.getData(
            url,
            queryParams = queryParams,
            responseClass = GooglePlacesResponse::class.java
        ) { success, response ->
            if (success && response != null) {
                try {
                    val placeNames = response.results.map { it.name }
                    val addresses = response.results.map { it.vicinity }
                    callback(placeNames, addresses)
                } catch (e: Exception) {
                    callback(emptyList(), emptyList())
                    Log.d("arieum", "exception Google palce api")
                }
            } else {
                callback(emptyList(), emptyList())
                Log.d("arieum", "Failed to Google palce api")
            }
        }
    }
}