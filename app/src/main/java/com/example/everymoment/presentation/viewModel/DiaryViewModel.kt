package com.example.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.vo.Category
import com.example.everymoment.data.model.network.dto.vo.DetailDiary
import com.example.everymoment.data.model.network.dto.vo.File
import com.example.everymoment.data.model.network.dto.request.PostFilesRequest
import com.example.everymoment.data.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {

    private var diaryId: Int? = null
    private lateinit var diaryInfo: DetailDiary
    private var isbookmarked: Boolean = false
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories
    private var imagesArray: List<File> = listOf()

    init {
        getCategories()
    }

    fun getFilesArray(): List<File> {
        return imagesArray
    }

    fun getCategoryId(categoryName: String): Int? {
        val idx = categories.value?.indexOfFirst {
            it.categoryName.equals(categoryName.substring(1), ignoreCase = true)
        }
        return idx?.let { categories.value?.get(it)?.id }
    }

    fun postCategory(categoryName: String) {
        viewModelScope.launch {
            diaryRepository.postCategory(categoryName) { success, response ->
                if (success && response != null) {
                    getCategories()
                }
            }
        }
    }

    fun delCategory(categoryId: Int) {
        viewModelScope.launch {
            diaryRepository.delCategory(categoryId) { success, response ->
                if (success && response != null) {
                    getCategories()
                }
            }
        }
    }

    fun getCategoryListSize(): Int? {
        return categories.value?.size
    }

    fun getDiaryId(): Int {
        return diaryId!!
    }

    fun getIsBookmarked(): Boolean {
        return isbookmarked
    }

    fun getDiary(): DetailDiary {
        return diaryInfo
    }

    fun updateBookmarkStatus() {
        viewModelScope.launch {
            diaryId?.let {
                isbookmarked != isbookmarked
                diaryRepository.updateBookmarkStatus(it) { _, _ ->
                }
            }
        }
    }

    suspend fun getDiaryinDetail(diaryId: Int?, nextlambda: (DetailDiary?) -> Unit) {
        this.diaryId = diaryId
        diaryId?.let {
            withContext(Dispatchers.IO) {
                var result: DetailDiary? = null
                diaryRepository.getDiaryinDetail(it) { success, response ->
                    Log.d("settle54", "success: $success")
                    if (success && response != null) {
                        Log.d("settle54", "success: ${response.info}")
                        result = response.info
                    }
                    result?.let {
                        Log.d("settle54", "lambda: $it")
                        diaryInfo = it
                        isbookmarked = it.bookmark
                        nextlambda(result)
                    }
                }
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            diaryRepository.getCategories() { success, response ->
                if (success && response != null) {
                    _categories.postValue(response.categories)
                }
            }
        }
    }

    fun getFiles(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.getFiles(diaryId) { success, response ->
                if (success && response != null) {
                    imagesArray = response.info
                }
            }
        }
    }

    fun postFiles(diaryId: Int, imgArray: List<File>) {
        viewModelScope.launch {
            val files = PostFilesRequest(imgArray)
            diaryRepository.postFiles(diaryId, files) { _, _ ->
            }
        }
    }

}