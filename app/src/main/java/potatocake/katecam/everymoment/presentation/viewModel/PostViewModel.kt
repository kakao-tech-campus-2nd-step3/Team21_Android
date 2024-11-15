package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import potatocake.katecam.everymoment.data.model.network.dto.request.PatchCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.Comment
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.Post
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import potatocake.katecam.everymoment.data.repository.PostRepository
import potatocake.katecam.everymoment.di.MyInfoRepositoryQualifier
import potatocake.katecam.everymoment.di.PostRepositoryQualifier
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    @PostRepositoryQualifier
    private val postRepository: PostRepository,
    @MyInfoRepositoryQualifier
    private val myInfoRepository: MyInfoRepository
) : ViewModel() {
    private var diaryId: Int? = null
    private var userId: Int? = null

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post
    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>> get() = _images

    private val _comments = MutableLiveData<CommentState>()
    val comments: LiveData<CommentState> get() = _comments
    private val _likeCnt = MutableLiveData<Int>()
    val likeCnt: LiveData<Int> get() = _likeCnt
    private val _commentCnt = MutableLiveData<Int>()
    val commentCnt: LiveData<Int> get() = _commentCnt

    data class PageState(var currentPage: Int, var nextPage: Int?, var isLoading: Boolean)
    var pageState = PageState(0, null, false)
    data class CommentState(var comments: List<Comment>, var scrollToBottom: Boolean)

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
                        Log.d("postViewModel", "$response")
                        toggleLikedState()
                        getLikeCnt()
                    }
                }
            }
        }
    }

    private fun toggleLikedState() {
        _post.value?.let {
            _post.postValue(it.copy(liked = !it.liked))
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
    fun getUserId() {
        viewModelScope.launch {
            myInfoRepository.getMyInfo { success, response ->
                if (success && response != null) {
                    userId = response.info.id
                }
            }
        }
    }

    fun checkIsUserId(commentUserId: Int): Boolean {
        userId?.let {
         return it == commentUserId
        }
        return false
    }

    fun getCommentCnt() {
        viewModelScope.launch {
            diaryId?.let {
                postRepository.getCommentCnt(diaryId!!) { success, response ->
                    Log.d("settle54", "success: $success")
                    if (success && response != null) {
                        Log.d("settle54", "success: ${response.commentCnt}")
                        _commentCnt.postValue(response.commentCnt)
                    }
                }
            }
        }
    }

    fun postComment(comment: String) {
        viewModelScope.launch {
            val request = PostCommentRequest(comment)
            postRepository.postComment(diaryId!!, request) { success, response ->
                if (success && response != null) {
                    getComments(true)
                    getCommentCnt()
                }
            }
        }
    }

    fun patchComment(commentId: Int, comment: String) {
        viewModelScope.launch {
            val request = PatchCommentRequest(comment)
            postRepository.patchComment(commentId, request) { success, response ->
                if (success && response != null) {
                }
            }
        }
    }

    fun delComment(commentId: Int) {
        viewModelScope.launch {
            postRepository.delComment(commentId) { success, response ->
                if (success && response != null) {
                    getComments(true)
                    getCommentCnt()
                }
            }
        }
    }

    fun getComments(scrollToBottom: Boolean = false) {
        if (pageState.isLoading) return
        pageState.isLoading = true
        viewModelScope.launch {
            Log.d("postViewModel", "pageState: $pageState")
            postRepository.getComments(diaryId!!, pageState.currentPage) { success, response ->
                if (success && response != null) {
                    val newComments = response.commentList.comments
                    Log.d("PostViewModel", "getComments: $newComments")

                    if (pageState.currentPage == 0) {
                        currentComments.clear()
                    }

                    currentComments.addAll(newComments)
                    _comments.postValue(CommentState(currentComments.toList(), scrollToBottom))

                    pageState.nextPage = response.commentList.next
                    pageState.isLoading = false
                }
            }
        }
    }

    fun loadNextComments() {
        if (pageState.nextPage != null) {
            pageState.currentPage = pageState.nextPage!!
            getComments()
        } else {
            Log.d("postViewModel", "다음 페이지가 없습니다.")
        }
    }

}