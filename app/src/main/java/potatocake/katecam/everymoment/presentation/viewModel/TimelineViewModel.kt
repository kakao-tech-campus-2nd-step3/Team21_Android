package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.api.GooglePlaceApiUtil
import potatocake.katecam.everymoment.data.model.network.dto.request.EmojiRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.LocationNameRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.Diary
import potatocake.katecam.everymoment.data.repository.DiaryRepository
import potatocake.katecam.everymoment.di.DiaryRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    @DiaryRepositoryQualifier
    private val diaryRepository: DiaryRepository
) : ViewModel() {
    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var nextPage = 1
    private var selectedDate: String? = null


    fun fetchDiaries(date: String) {
        selectedDate = date
        _isLoading.value = true
        viewModelScope.launch {
            diaryRepository.getDiaries(date) { success, response ->
                _isLoading.value = false
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                    Log.d("arieum", response.info.diaries.toString())
                    nextPage = response.info.next
                }
            }
        }
    }

    fun updateBookmarkStatus(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.updateBookmarkStatus(diaryId) { success, response ->
            }
        }
    }

    fun updateShareStatus(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.updateShareStatus(diaryId) { success, response ->
            }
        }
    }

    fun deleteDiary(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId) { success, response -> }
        }
    }

    fun getPlaceNamesForDiary(diaryId: Int, callback: (List<String>) -> Unit) {
        getDiaryLocation(diaryId) { latitude, longitude ->
            if (latitude == 0.0 && longitude == 0.0) {
                callback(listOf("우리집", "회사", "학교"))
            } else {
                getPlaceNames(latitude, longitude) { placeNames ->
                    callback(placeNames)
                }
            }
        }
    }

    private fun getDiaryLocation(diaryId: Int, callback: (Double, Double) -> Unit) {
        viewModelScope.launch {
            diaryRepository.getDiaryLocation(diaryId) { success, response ->
                if (success && response != null) {
                    callback(response.info.latitude, response.info.longitude)
                } else {
                    callback(0.0, 0.0)
                }
            }
        }
    }

    private fun getPlaceNames(
        latitude: Double,
        longitude: Double,
        callback: (List<String>) -> Unit
    ) {
        GooglePlaceApiUtil.getPlaceNamesFromCoordinates(longitude, latitude) { placeNames, _ ->
            callback(placeNames)
            Log.d("arieum", "google place names : $placeNames")
        }
    }

    fun updateDiaryLocation(diaryId: Int, locationName: String) {
        viewModelScope.launch {
            val request = LocationNameRequest(
                locationName = locationName
            )

            diaryRepository.patchLocationName(diaryId, request) { success, message ->
                if (success) {
                    Log.d("arieum", "Diary location updated successfully")
                } else {
                    Log.e("arieum", "$message")
                }
            }
        }
    }

    fun updateEmotions(diaryId: Int, selectedEmoji: String) {
        viewModelScope.launch {
            val request = EmojiRequest(
                emoji = selectedEmoji
            )

            diaryRepository.patchEmoji(diaryId, request) { success, message ->
                if (success) {
                    Log.d("arieum", "Diary emoji updated successfully")
                } else {
                    Log.e("arieum", "$message")
                }
            }
        }
    }

    fun fetchNextPage() {
        if (nextPage != 0 && _isLoading.value != true) {
            _isLoading.value = true
            viewModelScope.launch {
                diaryRepository.getDiariesWithPage(selectedDate!!, nextPage) { success, response ->
                    _isLoading.value = false
                    if (success && response != null) {
                        val currentList = _diaries.value.orEmpty()
                        _diaries.postValue(currentList + response.info.diaries)
                        nextPage = response.info.next
                    }
                }
            }
        }
    }
}