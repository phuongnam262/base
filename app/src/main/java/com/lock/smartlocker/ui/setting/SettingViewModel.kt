package com.lock.smartlocker.ui.setting

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SettingViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    fun getInformationStaff() {
        ioScope.launch {
            mLoading.postValue(true)

        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}