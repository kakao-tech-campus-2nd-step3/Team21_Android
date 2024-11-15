package potatocake.katecam.everymoment.services.location

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import potatocake.katecam.everymoment.GlobalApplication
import potatocake.katecam.everymoment.R
import potatocake.katecam.everymoment.data.model.network.api.GooglePlaceApiUtil
import potatocake.katecam.everymoment.data.model.network.api.NetworkUtil
import potatocake.katecam.everymoment.data.model.network.dto.vo.DiaryEntry
import potatocake.katecam.everymoment.data.model.network.dto.vo.LocationPoint
import potatocake.katecam.everymoment.presentation.view.main.MainActivity

class LocationService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var notificationManager: NotificationManager

    private var initialPlaceName: String? = null
    private var previousPlaceNames: List<String> = emptyList()
    private var isFirstLocationUpdateAfterChange = true

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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

        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            LOCATION_UPDATE_INTERVAL
        )
            .setMinUpdateIntervalMillis(LOCATION_UPDATE_INTERVAL)
            .setMaxUpdateDelayMillis(LOCATION_UPDATE_INTERVAL)
            .setWaitForAccurateLocation(false)
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            handler.looper
        )
    }

    private fun handleNewLocation(location: Location) {
        val jwtToken = GlobalApplication.prefs.getString("token", "null")

        val latitude = location.latitude
        val longitude = location.longitude

        GooglePlaceApiUtil.getPlaceNamesFromCoordinates(
            latitude,
            longitude
        ) { currentPlaceNames, currentAddresses ->
            if (currentPlaceNames.isNotEmpty()) {
                Log.d("myplace", "$currentPlaceNames")
                val currentPlace = currentPlaceNames.firstOrNull()
                val currentAddress = currentAddresses.firstOrNull()

                if (initialPlaceName == null || (currentPlace != null && !previousPlaceNames.contains(
                        currentPlace
                    ))
                ) {
                    isFirstLocationUpdateAfterChange = true
                    previousPlaceNames = currentPlaceNames
                    initialPlaceName = currentPlace
                    Log.d("arieum", "새 장소 측정: $initialPlaceName, $currentAddress 아직 전달 안함")
                } else {
                    if (isFirstLocationUpdateAfterChange) {
                        Log.d("arieum", "백엔드로 전달: $initialPlaceName, $currentAddress")

                        val locationData = DiaryEntry(
                            locationPoint = LocationPoint(latitude, longitude),
                            locationName = currentPlace ?: "알 수 없는 장소",
                            address = currentAddress ?: "알 수 없는 위치"
                        )

                        NetworkUtil.sendData(
                            "http://13.125.156.74:8080/api/diaries/auto",
                            jwtToken,
                            locationData
                        ) { success, code, message, infoObject ->
                            if (success) {
                                Log.d("arieum", "성공! 코드: $code, 메시지: $message, 정보: $infoObject")
                            } else {
                                Log.d("arieum", "실패!")
                            }
                        }

                        isFirstLocationUpdateAfterChange = false
                    } else {
                        Log.d("arieum", "세 번 이상 장소: $initialPlaceName, 더이상 전달 안함")
                    }
                }
            } else {
                // 장소 정보가 비어있는 경우 처리 - currentPlaceNames 비어있음
                Log.d("arieum", "장소 정보를 찾을 수 없음.")
            }

            // 현재 위치 정보로 알림 업데이트
            updateNotification(latitude, longitude, currentPlaceNames.firstOrNull() ?: "알 수 없는 장소")
        }
    }

    private fun createNotification(contentText: String): Notification {
        val channelId = "location_service_channel"
        val channelName = "Location Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // `FLAG_IMMUTABLE`는 Android 12 이상에서 필요합니다.
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("에브리모먼트")
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun updateNotification(latitude: Double, longitude: Double, placeName: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = createNotification("현재 ${placeName}에 머무르고 있어요!")
        notificationManager.notify(NOTIFICATION_ID, notification)
    }



    companion object {
        private const val NOTIFICATION_ID = 1
        private var LOCATION_UPDATE_INTERVAL = 15 * 60 * 1000L

        private const val CHANNEL_ID = "main_default_channel"
        private const val CHANNEL_NAME = "main channelName"

        fun setLocationUpdateInterval(interval: Long) {
            LOCATION_UPDATE_INTERVAL = interval
        }
    }
}