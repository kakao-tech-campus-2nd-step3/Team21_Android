package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.response.Diary
import com.example.everymoment.data.model.network.dto.vo.FilterState
import com.example.everymoment.data.repository.DiaryRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {
    private val _searchDiaries = MutableLiveData<List<Diary>>()
    val searchDiaries: LiveData<List<Diary>> get() = _searchDiaries

    private val _filterState = MutableLiveData<FilterState>()
    val filterState: LiveData<FilterState> = _filterState

    fun updateFilter(filterState: FilterState) {
        _filterState.value = filterState
        fetchSearchedDiaries(
            keyword = null,
            emoji = filterState.selectedEmotions.map { it.getEmotionUnicode() },
            category = filterState.selectedCategories,
            from = filterState.startDate,
            until = filterState.endDate,
            bookmark = filterState.isBookmarked
        )
    }

    fun fetchSearchedDiaries(
        keyword: String?,
        emoji: List<String>?,
        category: List<String>?,
        from: String?,
        until: String?,
        bookmark: Boolean?
    ) {
        viewModelScope.launch {
            diaryRepository.getSearchedDiaries(keyword, emoji, category, from, until, bookmark) { success, response ->
                if (success && response != null) {
                    _searchDiaries.postValue(response.info.diaries)
                }
            }
        }
    }
}