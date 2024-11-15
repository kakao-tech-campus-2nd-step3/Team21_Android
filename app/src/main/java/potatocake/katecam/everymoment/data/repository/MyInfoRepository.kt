package potatocake.katecam.everymoment.data.repository

import okhttp3.MultipartBody
import potatocake.katecam.everymoment.data.model.network.dto.response.MyInformationResponse
import potatocake.katecam.everymoment.data.model.network.dto.response.ServerResponse

interface MyInfoRepository {

    fun getMyInfo(
        callback: (Boolean, MyInformationResponse?) -> Unit
    )

    fun updateMyInfo(
        nickname: String? = null,
        profileImg: MultipartBody.Part? = null,
        callback: (Boolean, ServerResponse) -> Unit
    )
}