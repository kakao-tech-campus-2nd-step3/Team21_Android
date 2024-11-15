package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import potatocake.katecam.everymoment.data.model.network.dto.request.ManualDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary.PatchEditedDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.vo.Category
import potatocake.katecam.everymoment.data.model.network.dto.vo.DetailDiary
import potatocake.katecam.everymoment.data.repository.DiaryRepository
import potatocake.katecam.everymoment.di.DiaryRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    @DiaryRepositoryQualifier
    private val diaryRepository: DiaryRepository
) : ViewModel() {

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

    fun getDiaryinDetail(diaryId: Int?) {
        this.diaryId = diaryId
        diaryId?.let {
            diaryRepository.getDiaryinDetail(it) { success, response ->
                Log.d("settle54", "success: $success")
                if (success && response != null) {
                    Log.d("settle54", "success: ${response.info}")
                    val diary = response.info
                    _diary.postValue(diary)
                }
            }
        }
    }

    fun getFiles(diaryId: Int?) {
        viewModelScope.launch {
            diaryId?.let {
                diaryRepository.getFiles(diaryId) { success, response ->
                    if (success && response != null) {
                        val imageUrls = response.info.map { it.imageUrl }
                        _images.postValue(imageUrls)
                        Log.d("getFiles", "${images.value}")
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

    fun postManualDiary(request: ManualDiaryRequest, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            diaryRepository.postNewManualDiary(request) { success, message ->
                Log.d("arieum", message.toString())
                callback(success)
            }
        }
    }
}