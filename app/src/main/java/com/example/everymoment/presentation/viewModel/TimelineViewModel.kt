package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.data.model.network.dto.response.Diary
import kotlinx.coroutines.launch

class TimelineViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {
    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries


    fun fetchDiaries(date: String) {
        viewModelScope.launch {
            diaryRepository.getDiaries(date) { success, response ->
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
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

    fun deleteDiary(diaryId: Int){
        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId) { success, response -> }
        }
    }
}