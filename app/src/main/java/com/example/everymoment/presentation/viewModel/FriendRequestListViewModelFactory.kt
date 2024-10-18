package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.data.repository.FriendRepository

class FriendRequestListViewModelFactory(
    private val friendRepository: FriendRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendRequestListViewModel::class.java)) {
            return FriendRequestListViewModel(friendRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}