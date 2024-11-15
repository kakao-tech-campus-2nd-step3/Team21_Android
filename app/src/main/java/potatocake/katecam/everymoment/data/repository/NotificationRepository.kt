package potatocake.katecam.everymoment.data.repository

import potatocake.katecam.everymoment.data.model.network.dto.response.NotificationResponse

interface NotificationRepository {
    fun getNotificationList(
        callback: (Boolean, NotificationResponse?) -> Unit
    )

    fun readNotification(
        notificationId: Int,
    )

}