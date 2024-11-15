package potatocake.katecam.everymoment.data.repository

import potatocake.katecam.everymoment.data.model.network.dto.request.PatchCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.PostCommentRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.GetCommentCntResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetFilesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getComments.GetCommentsResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail.GetFriendDiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.getLikeCnt.GetLikeCountResponse

interface PostRepository {
    fun getDiaryinDetail(
        diaryId: Int,
        callback: (Boolean, GetFriendDiaryResponse?) -> Unit
    )

    fun getFiles(diaryId: Int, callback: (Boolean, GetFilesResponse?) -> Unit)

    fun postLike(diaryId: Int, callback: (Boolean, String?) -> Unit)

    fun getLikeCnt(diaryId: Int, callback: (Boolean, GetLikeCountResponse?) -> Unit)

    fun getCommentCnt(diaryId: Int, callback: (Boolean, GetCommentCntResponse?) -> Unit)

    fun postComment(diaryId: Int, request: PostCommentRequest, callback: (Boolean, String?) -> Unit)

    fun patchComment(commentId: Int, request: PatchCommentRequest, callback: (Boolean, String?) -> Unit)

    fun delComment(commentId: Int, callback: (Boolean, String?) -> Unit)

    fun getComments(diaryId:Int, key: Int, callback: (Boolean, GetCommentsResponse?) -> Unit)

}