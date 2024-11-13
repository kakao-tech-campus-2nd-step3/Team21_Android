package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import potatocake.katecam.everymoment.data.model.network.dto.response.Diary
import potatocake.katecam.everymoment.data.repository.FriendDiaryRepository
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.data.model.network.dto.response.Friends
import kotlinx.coroutines.launch

class ShareViewModel(
    private val friendDiaryRepository: FriendDiaryRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val _selectedFriendName = MutableLiveData<String>()
    val selectedFriendName: LiveData<String> get() = _selectedFriendName
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends

    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var nextPage = 1
    var currentFriendId: Int? = null

    fun setSelectedFriendName(nickName: String) {
        viewModelScope.launch {
            _selectedFriendName.postValue(nickName)
        }
    }

    fun fetchFriendsList() {
        viewModelScope.launch {
            friendRepository.getFriendsList() { success, response ->
                if (success && response != null) {
                    _friends.postValue(response.info.friends)
                }
            }
        }
    }

    fun fetchFriendDiaryList(friendId: Int) {
        currentFriendId = friendId
        _isLoading.value = true
        viewModelScope.launch {
            friendDiaryRepository.getFriendDiaries(friendId) { success, response ->
                _isLoading.value = false
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                    nextPage = response.info.next
                }
            }
        }
    }

    fun fetchTotalFriendDiaryList(date: String){
        _isLoading.value = true
        viewModelScope.launch {
            friendDiaryRepository.getTotalFriendDiaries(date) { success, response ->
                _isLoading.value = false
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                    Log.d("arieum", response.info.diaries.toString())
                    nextPage = response.info.next
                }
            }
        }
    }

    fun fetchNextPage() {
        if (nextPage != 0 && _isLoading.value != true) {
            _isLoading.value = true
            viewModelScope.launch {
                friendDiaryRepository.getFriendDiariesWithPage(currentFriendId!!, nextPage) { success, response ->
                    _isLoading.value = false
                    if (success && response != null) {
                        val currentList = _diaries.value.orEmpty()
                        _diaries.postValue(currentList + response.info.diaries)
                        nextPage = response.info.next
                    } else {
                        _diaries.postValue(emptyList())
                    }
                }
            }
        }
    }
}