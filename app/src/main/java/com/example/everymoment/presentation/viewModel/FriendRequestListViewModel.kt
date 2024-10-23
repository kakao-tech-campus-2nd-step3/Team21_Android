package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.model.network.dto.response.FriendRequests
import kotlinx.coroutines.launch

class FriendRequestListViewModel(private val friendRepository: FriendRepository) : ViewModel() {
    private val _requestedFriend = MutableLiveData<List<FriendRequests>>()
    val requestedFriend: LiveData<List<FriendRequests>> get() = _requestedFriend

    private val _acceptFriendRequestResult = MutableLiveData<FriendRequestResult>()
    val acceptFriendRequestResult: LiveData<FriendRequestResult> = _acceptFriendRequestResult
    private val _rejectFriendRequestResult = MutableLiveData<FriendRequestResult>()
    val rejectFriendRequestResult: LiveData<FriendRequestResult> = _rejectFriendRequestResult


    fun fetchFriendRequestList() {
        viewModelScope.launch {
            friendRepository.getFriendRequestList() { success, response ->
                if (success && response != null) {
                    _requestedFriend.postValue(response.info.friendRequests)
                }
            }
        }
    }

    fun acceptFriendRequest(requestId: Int) {
        viewModelScope.launch {
            friendRepository.acceptFriendRequest(requestId) { success, message ->
                if (success) {
                    _acceptFriendRequestResult.value = FriendRequestResult.Success(message)
                    fetchFriendRequestList()
                } else {
                    _acceptFriendRequestResult.value =
                        FriendRequestResult.Error(message ?: "알 수 없는 오류가 발생했습니다.")
                }
            }
        }
    }

    fun rejectFriendRequest(requestId: Int) {
        viewModelScope.launch {
            friendRepository.rejectFriendRequest(requestId) { success, message ->
                if (success) {
                    _rejectFriendRequestResult.value = FriendRequestResult.Success(message)
                    fetchFriendRequestList()
                } else {
                    _rejectFriendRequestResult.value =
                        FriendRequestResult.Error(message ?: "알 수 없는 오류가 발생했습니다.")
                }
            }
        }
    }
}

sealed class FriendRequestResult {
    data class Success(val message: String?) : FriendRequestResult()
    data class Error(val message: String) : FriendRequestResult()
}