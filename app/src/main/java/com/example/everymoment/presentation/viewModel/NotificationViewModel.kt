package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.MyNotification
import com.example.everymoment.data.repository.NotificationRepository
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
                    _notifications.postValue(response.info)
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
}