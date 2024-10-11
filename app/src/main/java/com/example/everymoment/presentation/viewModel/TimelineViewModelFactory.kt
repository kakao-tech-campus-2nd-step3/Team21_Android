package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.everymoment.data.repository.DiaryRepository

class TimelineViewModelFactory(
    private val diaryRepository: DiaryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            return TimelineViewModel(diaryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}