package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.model.network.dto.response.Friends
import kotlinx.coroutines.launch

class FriendsListViewModel(private val friendRepository: FriendRepository) : ViewModel() {
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends

    fun fetchFriendsList() {
        viewModelScope.launch {
            friendRepository.getFriendsList() { success, response ->
                if (success && response != null) {
                    _friends.postValue(response.info.friends)
                }
            }
        }
    }

    fun deleteFriend(friendId: Int) {
        viewModelScope.launch {
            friendRepository.deleteFriend(friendId) { success, response -> }
        }
    }
}