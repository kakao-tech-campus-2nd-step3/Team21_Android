package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.NotificationRepository

class NotificationViewModelFactory(
    private val notificationRepository: NotificationRepository,
    private val friendRepository: FriendRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepository, friendRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}