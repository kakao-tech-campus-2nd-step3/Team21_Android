package potatocake.katecam.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import potatocake.katecam.everymoment.data.model.network.dto.response.MyInformation
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class SettingViewModel(private val myInfoRepository: MyInfoRepository): ViewModel() {
    private val _myInfo = MutableLiveData<MyInformation>()
    val myInfo: MutableLiveData<MyInformation> get() = _myInfo
    fun fetchMyInfo(){
        viewModelScope.launch {
            myInfoRepository.getMyInfo { success, response ->
                if (success && response != null) {
                    _myInfo.postValue(response.info)
                }
            }
        }
    }

    fun updateProfile(nickname: String?, profileImagePart: MultipartBody.Part?) {
        val nicknameRequestBody = nickname?.toRequestBody("text/plain".toMediaTypeOrNull())

        viewModelScope.launch {
            myInfoRepository.updateMyInfo(nicknameRequestBody, profileImagePart) { success, response ->
                if (success) {
                    Log.d("arieum", "Profile updated successfully")
                    Log.d("arieum", profileImagePart.toString())
                } else {
                    Log.d("arieum", "Failed to update profile, ${response.message}")
                }
            }
        }
    }

}