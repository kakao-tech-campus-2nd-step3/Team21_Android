package potatocake.katecam.everymoment.data.repository

import okhttp3.MultipartBody
import potatocake.katecam.everymoment.data.model.network.dto.request.EmojiRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.LocationNameRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.ManualDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary.PatchEditedDiaryRequest
import potatocake.katecam.everymoment.data.model.network.dto.response.CoordinatesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.DiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetCategoriesResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetDetailDiaryResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.GetFilesResponse

interface DiaryRepository {

    fun getDiaries(
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    )

    fun updateBookmarkStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun updateShareStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun deleteDiary(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    )

    fun getDiaryinDetail(
        diaryId: Int,
        callback: (Boolean, GetDetailDiaryResponse?) -> Unit
    )

    fun postCategory(
        categoryName: String, callback: (Boolean, String?) -> Unit
    )

    fun delCategory(categoryId: Int, callback: (Boolean, String?) -> Unit)

    fun getCategories(callback: (Boolean, GetCategoriesResponse?) -> Unit)

    fun getFiles(diaryId: Int, callback: (Boolean, GetFilesResponse?) -> Unit)

    fun patchFiles(diaryId: Int, files: List<MultipartBody.Part>, callback: (Boolean, String?) -> Unit)

    fun getSearchedDiaries(
        keyword: String?,
        emoji: String?,
        category: String?,
        from: String?,
        until: String?,
        bookmark: Boolean?,
        callback: (Boolean, DiaryResponse?) -> Unit
    )

    fun patchEditedDiary(
        diaryId: Int,
        request: PatchEditedDiaryRequest,
        callback: (Boolean, String?) -> Unit
    )

    fun getDiaryLocation(diaryId: Int, callback: (Boolean, CoordinatesResponse?) -> Unit)

    fun getDiariesWithPage(
        date: String,
        nextPage: Int,
        callback: (Boolean, DiaryResponse?) -> Unit)

    fun patchEmoji(
        diaryId: Int,
        request: EmojiRequest,
        callback: (Boolean, String?) -> Unit
    )

    fun patchLocationName(
        diaryId: Int,
        request: LocationNameRequest,
        callback: (Boolean, String?) -> Unit
    )

    fun postNewManualDiary(
        request: ManualDiaryRequest,
        callback: (Boolean, String?) -> Unit
    )
}