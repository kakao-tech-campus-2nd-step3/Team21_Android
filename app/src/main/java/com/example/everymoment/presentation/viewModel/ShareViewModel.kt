package com.example.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.FriendDiaryRepository
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.repository.Friends
import kotlinx.coroutines.launch

class ShareViewModel(
    private val friendDiaryRepository: FriendDiaryRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends

    fun fetchFriendsList() {
        viewModelScope.launch {
            try {
                friendRepository.getFriendsList(
                    onSuccess = { response ->
                        _friends.postValue(response.info.friends)
                                Log.d("arieum", "${response.info.friends}")},
                    onError = { errorMessage -> }
                )
            } catch (e: Exception) {
                Log.d("arieum", "Failed to fetch friends list: ${e.message}")
            }
        }
    }

    fun fetchFriendDiaryList() {}
}