package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.FriendRequests
import kotlinx.coroutines.launch

class FriendRequestListViewModel(private val friendRepository: FriendRepository) : ViewModel() {
    private val _requestedFriend = MutableLiveData<List<FriendRequests>>()
    val requestedFriend: LiveData<List<FriendRequests>> get() = _requestedFriend

    fun fetchFriendRequestList() {
        viewModelScope.launch {
            friendRepository.getFriendRequestList() { success, response ->
                if (success && response != null) {
                    _requestedFriend.postValue(response.info.friendRequests)
                }
            }
        }
    }
}