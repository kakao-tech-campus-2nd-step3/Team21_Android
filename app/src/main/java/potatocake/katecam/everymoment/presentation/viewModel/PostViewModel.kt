package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.Comment
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.Post
import potatocake.katecam.everymoment.data.repository.PostRepository
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    private var diaryId: Int? = null

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post
    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>> get() = _images

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments
    private val _likeCnt = MutableLiveData<Int>()
    val likeCnt: LiveData<Int> get() = _likeCnt

    private var key: Int = 0
    private var isLoading = false
    private var commentHasNextPage = true
    private var commentHasPreviousPage = false
    private val currentComments: MutableList<Comment> = mutableListOf()

    /**
     * 포스트
     */
    fun getFriendDiaryinDetail(diaryId: Int?) {
        diaryId?.let {
            this.diaryId = diaryId
            postRepository.getDiaryinDetail(it) { success, response ->
                Log.d("settle54", "success: $success")
                if (success && response != null) {
                    Log.d("settle54", "success: ${response.info}")
                    _post.postValue(response.info)
                    _likeCnt.postValue(response.info.likeCount.likeCount)
                }
            }
        }
    }

    fun getFiles(diaryId: Int?) {
        viewModelScope.launch {
            diaryId?.let {
                postRepository.getFiles(diaryId) { success, response ->
                    if (success && response != null) {
                        val imageUrls = response.info.map { it.imageUrl }
                        _images.postValue(imageUrls)
                        Log.d("getFiles", "${images.value}")
                    }
                }
            }
        }
    }

    fun postLike() {
        viewModelScope.launch {
            diaryId?.let {
                postRepository.postLike(diaryId!!) { success, response ->
                    if (success && response != null) {
                        getLikeCnt()
                    }
                }
            }
        }
    }

    fun getLikeCnt() {
        viewModelScope.launch {
            diaryId?.let {
                postRepository.getLikeCnt(diaryId!!) { success, response ->
                    Log.d("settle54", "success: $success")
                    if (success && response != null) {
                        Log.d("settle54", "success: ${response.likeCount.likeCount}")
                        _likeCnt.postValue(response.likeCount.likeCount)
                    }
                }
            }
        }
    }



    /**
     * 댓글
     */
    fun postComment(comment: String) {
        viewModelScope.launch {
            val request = PostCommentRequest(comment)
            postRepository.postComment(diaryId!!, request) { success, response ->
                if (success && response != null) {
                    getComments()
                }
            }
        }
    }

    fun delComment(commentId: Int) {
        viewModelScope.launch {
            postRepository.delComment(commentId) { success, response ->
                if (success && response != null) {
                    getComments()
                }
            }
        }
    }

    fun getComments() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            Log.d("postViewModel", "key: $key")
            postRepository.getComments(diaryId!!, key) { success, response ->
                if (success && response != null) {
                    val newComments = response.commentList.comments
                    Log.d("PostViewModel", "getComments: $newComments")

                    if (key == 0) {
                        currentComments.clear()
                    }

                    currentComments.addAll(newComments)
                    _comments.postValue(currentComments.toList())

                    commentHasNextPage = response.commentList.next != null
                    commentHasPreviousPage = key > 0
                    if (commentHasNextPage) {
                        key += 1
                    }
                    isLoading = false
                }
            }
        }
    }

    fun loadNextComments() {
        if (commentHasNextPage) {
            getComments()
        } else {
            Log.d("postViewModel", "다음 페이지가 없습니다.")
        }
    }

    fun loadPreviousComments() {
        if (key > 0) {
            key -= 1
            getComments()
        } else {
            Log.d("postViewModel", "이전 페이지가 없습니다.")
        }
    }

}