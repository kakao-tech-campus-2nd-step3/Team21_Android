package potatocake.katecam.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.dto.response.MyNotification
import potatocake.katecam.everymoment.data.model.network.vo.NotificationTypeConstants
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.data.repository.NotificationRepository
import potatocake.katecam.everymoment.di.FriendRepositoryQualifier
import potatocake.katecam.everymoment.di.NotificationRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @NotificationRepositoryQualifier
    private val notificationRepository: NotificationRepository,
    @FriendRepositoryQualifier
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