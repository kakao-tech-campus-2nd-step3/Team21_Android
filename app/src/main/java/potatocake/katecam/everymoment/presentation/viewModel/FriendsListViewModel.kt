package potatocake.katecam.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.dto.response.Friends
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.di.FriendRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class FriendsListViewModel @Inject constructor(
    @FriendRepositoryQualifier
    private val friendRepository: FriendRepository
) : ViewModel() {
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