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
    private val _selectedFriendPosition = MutableLiveData<Int>()
    val selectedFriendPosition: LiveData<Int> get() = _selectedFriendPosition
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends

    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    private val _isFriendDiaryListLoading = MutableLiveData<Boolean>()
    val isFriendDiaryListLoading: LiveData<Boolean> get() = _isFriendDiaryListLoading

    private val _isFriendListLoading = MutableLiveData<Boolean>()
    val isFriendListLoading: LiveData<Boolean> get() = _isFriendListLoading

    private var nextDiaryPage = 1
    private var nextFriendPage = 1
    var currentFriendId: Int? = null

    fun setSelectedFriendName(nickName: String) {
        viewModelScope.launch {
            _selectedFriendName.postValue(nickName)
        }
    }

    fun setSelectedFriendPosition(position: Int) {
        viewModelScope.launch {
            _selectedFriendPosition.postValue(position)
        }
    }

    fun fetchFriendsList() {
        _isFriendListLoading.value = true
        viewModelScope.launch {
            friendRepository.getFriendsList() { success, response ->
                _isFriendListLoading.value = false
                if (success && response != null) {
                    _friends.postValue(response.info.friends)
                    nextFriendPage = response.info.next
                }
            }
        }
    }

    fun fetchFriendDiaryList(friendId: Int) {
        currentFriendId = friendId
        _isFriendDiaryListLoading.value = true
        viewModelScope.launch {
            friendDiaryRepository.getFriendDiaries(friendId) { success, response ->
                _isFriendDiaryListLoading.value = false
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                    nextDiaryPage = response.info.next
                }
            }
        }
    }

    fun fetchTodayFriendDiaryList(date: String){
        _isFriendDiaryListLoading.value = true
        viewModelScope.launch {
            Log.d("shareViewModel", "fetchTotalFriendDiaryList")
            friendDiaryRepository.getTotalFriendDiaries(date) { success, response ->
                _isFriendDiaryListLoading.value = false
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                    Log.d("arieum", response.info.diaries.toString())
                    nextDiaryPage = response.info.next
                }
            }
        }
    }

    fun fetchFriendDiaryNextPage() {
        if (nextDiaryPage != 0 && _isFriendDiaryListLoading.value != true) {
            _isFriendDiaryListLoading.value = true
            viewModelScope.launch {
                friendDiaryRepository.getFriendDiariesWithPage(currentFriendId!!, nextDiaryPage) { success, response ->
                    _isFriendDiaryListLoading.value = false
                    if (success && response != null) {
                        val currentList = _diaries.value.orEmpty()
                        _diaries.postValue(currentList + response.info.diaries)
                        nextDiaryPage = response.info.next
                    } else {
                        _diaries.postValue(emptyList())
                    }
                }
            }
        }
    }

    fun fetchFriendsNextPage() {
        if (nextFriendPage != 0 && _isFriendListLoading.value != true) {
            _isFriendListLoading.value = true
            viewModelScope.launch {
                friendRepository.getFriendsListWithPage(nextFriendPage) { success, response ->
                    _isFriendListLoading.value = false
                    if (success && response != null) {
                        val currentFriends = _friends.value.orEmpty()
                        _friends.postValue(currentFriends + response.info.friends)
                        nextFriendPage = response.info.next
                    }
                }
            }
        }
    }
}