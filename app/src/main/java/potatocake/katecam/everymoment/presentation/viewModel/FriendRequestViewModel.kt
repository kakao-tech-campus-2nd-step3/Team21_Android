package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.dto.response.Member
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.di.FriendRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    @FriendRepositoryQualifier
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val _members = MutableLiveData<List<Member>>()
    val members: LiveData<List<Member>> get() = _members

    fun fetchMembers() {
        viewModelScope.launch {
            friendRepository.getMembers() { success, response ->
                if (success && response != null && response.info != null) {
                    _members.postValue(response.info.members)
                } else {
                    Log.e("FriendRequestViewModel", "Failed to fetch member")
                    _members.postValue(emptyList())
                }
            }
        }
    }

    fun sendFriendRequest(memberId: Int) {
        viewModelScope.launch {
            friendRepository.sendFriendRequest(memberId) { success, response ->
                if (success && response != null && response.info != null) {
                    _members.postValue(response.info.members)
                } else {
                    Log.e("FriendRequestViewModel", "Failed to send friend request")
                }
            }
        }
    }
}
