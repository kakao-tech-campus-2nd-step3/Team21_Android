package potatocake.katecam.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import potatocake.katecam.everymoment.data.model.network.vo.NotificationTypeConstants
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.data.model.network.dto.response.MyNotification
import potatocake.katecam.everymoment.data.repository.NotificationRepository
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val notificationRepository: NotificationRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val _notifications = MutableLiveData<List<MyNotification>>()
    val notifications: LiveData<List<MyNotification>> get() = _notifications

    fun fetchNotifications(){
        viewModelScope.launch {
            notificationRepository.getNotificationList { success, response ->
                if (success && response != null) {
                    val filteredNotifications = response.info.filter { notification ->
                        notification.type != NotificationTypeConstants.MOOD_CHECK && !notification.read }
                    _notifications.postValue(filteredNotifications)
                }
            }
        }
    }

    fun acceptFriendRequest(requestId: Int) {
        viewModelScope.launch {
            friendRepository.acceptFriendRequest(requestId) { success, message ->
                if (success) {

                } else {

                }
            }
        }
    }

    fun rejectFriendRequest(requestId: Int) {
        viewModelScope.launch {
            friendRepository.rejectFriendRequest(requestId) { success, message ->
                if (success) {

                } else {

                }
            }
        }
    }

    fun readNotification(notificationId: Int) {
        viewModelScope.launch {
            notificationRepository.readNotification(notificationId)
        }
    }
}