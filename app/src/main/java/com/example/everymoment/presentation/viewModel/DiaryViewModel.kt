package com.example.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.request.postEditDiary.PatchEditedDiaryRequest
import com.example.everymoment.data.model.network.dto.vo.Category
import com.example.everymoment.data.model.network.dto.vo.DetailDiary
import com.example.everymoment.data.repository.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DiaryViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {

    private var diaryId: Int? = null

    private val _diary = MutableLiveData<DetailDiary>()
    val diary: LiveData<DetailDiary> get() = _diary

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>> get() = _images

    init {
        getCategories()
    }

    /**
     * 카테고리
     */
    fun getCategoryId(categoryName: String?): Int? {
        val idx = categories.value?.indexOfFirst {
            it.categoryName.equals(categoryName?.substring(1), ignoreCase = true)
        }
        return idx?.let { categories.value?.get(it)?.id }
    }

    fun getCategoryName(categoryId: Int?): String? {
        val name = categories.value?.find { it.id == categoryId }?.categoryName
        return name
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

    fun getCategories() {
        viewModelScope.launch {
            diaryRepository.getCategories() { success, response ->
                if (success && response != null) {
                    _categories.postValue(response.categories)
                }
            }
        }
    }

    fun getCategoryListSize(): Int? {
        return categories.value?.size
    }


    /**
     * 일기
     */
    fun getDiaryId(): Int {
        return diaryId!!
    }

    fun getDiary(): DetailDiary {
        return diary.value!!
    }


    fun updateBookmarkStatus() {
        viewModelScope.launch {
            diaryId?.let {
                _diary.value = _diary.value?.copy(bookmark = _diary.value?.bookmark?.not() ?: false)
                diaryRepository.updateBookmarkStatus(it) { _, _ ->
                }
            }
        }
    }

    suspend fun getDiaryinDetail(diaryId: Int?, callback: (DetailDiary) -> Unit) {
        this.diaryId = diaryId
        diaryId?.let {
            val result = withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    diaryRepository.getDiaryinDetail(it) { success, response ->
                        Log.d("settle54", "success: $success")
                        if (success && response != null) {
                            Log.d("settle54", "success: ${response.info}")
                            continuation.resume(response.info)
                        } else {
                            continuation.resume(null)
                        }
                    }
                }
            }

            result?.let { diary ->
                Log.d("settle54", "lambda: $diary")
                _diary.value = diary
                callback.invoke(diary)
            }
        }
    }

    fun getFiles(diaryId: Int?, callback: (List<String>) -> Unit) {
        viewModelScope.launch {
            diaryId?.let {
                diaryRepository.getFiles(diaryId) { success, response ->
                    if (success && response != null) {
                        val imageUrls = response.info.map { it.imageUrl }
                        _images.postValue(imageUrls)
                        Log.d("getFiles", "${images.value}")
                        callback.invoke(imageUrls)
                    } else {
                        callback.invoke(emptyList())
                    }
                }
            }
        }
    }

    fun getImages(): List<String>? {
        return images.value
    }

    fun patchFiles(files: List<MultipartBody.Part>, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            diaryRepository.patchFiles(getDiaryId(), files) { success, _ ->
                callback(success)
            }
        }
    }

    fun patchViewModelFiles(files: List<String>) {
        viewModelScope.launch {
            Log.d("settle54", "patch: $diary")
            _images.postValue(files)
        }
    }

    fun patchViewModelDiary(diary: DetailDiary) {
        viewModelScope.launch {
            Log.d("settle54", "patch: $diary")
            _diary.postValue(diary)
        }
    }

    fun patchEditedDiary(request: PatchEditedDiaryRequest, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d("settle54", "success: $request")
            diaryRepository.patchEditedDiary(diaryId!!, request) { success, _ ->
                callback(success)
            }
        }
    }

}