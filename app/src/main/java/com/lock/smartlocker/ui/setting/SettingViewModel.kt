package com.lock.smartlocker.ui.setting

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    var backgroundMusic = MutableLiveData<String>("https://codeskulptor-demos.commondatastorage.googleapis.com/GalaxyInvaders/theme_01.mp3")
    var mediaPath = MutableLiveData<String>("https://cdn.pixabay.com/video/2023/11/23/190444-888131647_large.mp4")

    fun getInformationStaff() {
        ioScope.launch {
            mLoading.postValue(true)

        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}