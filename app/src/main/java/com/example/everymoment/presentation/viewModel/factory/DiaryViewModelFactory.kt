package com.example.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.presentation.viewModel.DiaryViewModel

class DiaryViewModelFactory (
    private val diaryRepository: DiaryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(diaryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}