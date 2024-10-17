package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.ViewModel
import com.example.everymoment.data.repository.FriendDiaryRepository
import com.example.everymoment.data.repository.FriendRepository

class ShareViewModel(
    private val friendDiaryRepository: FriendDiaryRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {

}