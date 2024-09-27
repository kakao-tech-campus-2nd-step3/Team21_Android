package com.example.everymoment

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread

    private var initialPlaceName: String? = null
    private var isFirstLocationUpdateAfterChange = true

    override fun onCreate() {
        super.onCreate()

        initializeLocationComponents()
        startLocationUpdates()
        startForeground(NOTIFICATION_ID, createNotification("위치 서비스 시작"))
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        handlerThread.quitSafely()
    }

    private fun initializeLocationComponents() {
        handlerThread = HandlerThread("LocationServiceThread").apply { start() }
        handler = Handler(handlerThread.looper)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, LOCATION_UPDATE_INTERVAL)
            .setMinUpdateIntervalMillis(LOCATION_UPDATE_INTERVAL)
            .setMaxUpdateDelayMillis(LOCATION_UPDATE_INTERVAL)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    handleNewLocation(location)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, handler.looper)
    }

    private fun handleNewLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude

        getPlaceNamesFromCoordinates(latitude, longitude) { currentPlaceNames ->
            if (currentPlaceNames.isNotEmpty()) {
                Log.d("myplace", "$currentPlaceNames")
                val currentPlace = currentPlaceNames.firstOrNull()

                // 처음 위치 측정이거나 현재 위치가 이전과 다를 때
                if (initialPlaceName == null || (currentPlace != null && !currentPlaceNames.contains(initialPlaceName!!))) {
                    initialPlaceName = currentPlace
                    Log.d("arieum", "새 장소 측정: $initialPlaceName, 아직 전달 안함")
                } else {
                    // 장소가 같을 때
                    if (isFirstLocationUpdateAfterChange) {
                        Log.d("arieum", "백엔드로 전달: $initialPlaceName")
                        isFirstLocationUpdateAfterChange = false // 첫 전달 이후에는 false로 설정하여 다시 전달 안함
                    } else {
                        Log.d("arieum", "세 번 이상 장소: $initialPlaceName, 더이상 전달 안함")
                    }
                }
            } else {
                // 장소 정보가 비어있는 경우 처리 - currentPlaceNames 비어있음
                Log.d("arieum", "장소 정보를 찾을 수 없음.")
            }

            // 현재 위치 정보로 알림 업데이트
            updateNotification(latitude, longitude, initialPlaceName ?: "알 수 없는 장소")
        }
    }

    private fun createNotification(contentText: String): Notification {
        val channelId = "location_service_channel"
        val channelName = "Location Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("위치 서비스")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(latitude: Double, longitude: Double, placeName: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification("위치: $placeName (위도: $latitude, 경도: $longitude)")
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getPlaceNamesFromCoordinates(
        latitude: Double,
        longitude: Double,
        callback: (List<String>) -> Unit
    ) {
        val apiKey = BuildConfig.API_KEY
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=$latitude,$longitude&language=ko&rankby=distance&key=$apiKey"

        val request = Request.Builder().url(url).build()
        OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(emptyList())
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val jsonObject = JSONObject(responseData)
                    val results = jsonObject.getJSONArray("results")
                    val placeNames = mutableListOf<String>()

                    for (i in 0 until results.length()) {
                        val place = results.getJSONObject(i)
                        val name = place.getString("name")
                        placeNames.add(name)
                    }
                    callback(placeNames)
                } else {
                    callback(emptyList())
                }
            }
        })
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val LOCATION_UPDATE_INTERVAL = 5 * 60 * 1000L // 5분
    }
}