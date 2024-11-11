package potatocake.katecam.everymoment.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.MyInfoRepository

class SettingViewModelFactory(
    private val myInfoRepository: MyInfoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(myInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}