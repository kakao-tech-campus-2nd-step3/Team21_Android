package com.example.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.response.Member
import com.example.everymoment.data.model.network.dto.response.MemberResponse
import com.example.everymoment.data.repository.FriendRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendRequestViewModel(private val friendRepository: FriendRepository) : ViewModel() {
    private val _members = MutableLiveData<List<Member>>()
    val members: LiveData<List<Member>> get() = _members

    fun fetchMembers() {
        viewModelScope.launch {
            friendRepository.getMembers() { success, response ->
                if (success && response != null) {
                    _members.postValue(response.info.members)
                }
            }
        }
    }

    fun sendFriendRequest(memberId: Int) {
        viewModelScope.launch {
            friendRepository.sendFriendRequest(memberId) { success, response ->
                if (success && response != null) {
                    _members.postValue(response.info.members)
                }
            }
        }
    }
}
