package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.Friends
import com.example.everymoment.data.repository.FriendsListResponse
import kotlinx.coroutines.launch

class FriendsListViewModel(private val friendRepository: FriendRepository) : ViewModel() {
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchFriendsList() {
        viewModelScope.launch {
            try {
                friendRepository.getFriendsList(
                    onSuccess = { response ->
                        _friends.postValue(response.info.friends)
                    },
                    onError = { errorMessage ->
                        _error.postValue(errorMessage)
                    }
                )
            } catch (e: Exception) {
                _error.postValue("Failed to fetch friends list: ${e.message}")
            }
        }
    }

    fun deleteFriend(FriendId: Int){
        viewModelScope.launch {
            friendRepository.deleteFriend(FriendId) { success, response -> }
        }
    }
}