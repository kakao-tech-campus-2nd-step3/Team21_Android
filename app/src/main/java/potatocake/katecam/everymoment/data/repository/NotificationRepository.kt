package potatocake.katecam.everymoment.data.repository

import android.util.Log
import potatocake.katecam.everymoment.services.location.GlobalApplication
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.NotificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationRepository {
    private val apiService: PotatoCakeApiService = NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token = "Bearer $jwtToken"
    fun getNotificationList(
        callback: (Boolean, NotificationResponse?) -> Unit
    ){
        apiService.getNotifications(token).enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(p0: Call<NotificationResponse>, p1: Response<NotificationResponse>) {
                if (p1.isSuccessful) {
                    Log.d("arieum", "${p1.body()}")
                    callback(true, p1.body())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<NotificationResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to fetch notification list: ${p1.message}")
                callback(false, null)
            }
        })
    }

    fun readNotification(
        notificationId: Int,
    ) {
        apiService.readNotification(token, notificationId).enqueue(object : Callback<ServerResponse> {
            override fun onResponse(p0: Call<ServerResponse>, p1: Response<ServerResponse>) {
                if (p1.isSuccessful){
                    Log.d("arieum", "${p1.body()}")
                } else {
                    Log.d("arieum", "Failed to Patch Notificationid")
                }
            }

            override fun onFailure(p0: Call<ServerResponse>, p1: Throwable) {
            }
        })
    }
}