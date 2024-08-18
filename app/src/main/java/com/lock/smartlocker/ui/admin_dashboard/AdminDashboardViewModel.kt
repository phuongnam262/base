package com.lock.smartlocker.ui.admin_dashboard

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    val otpText = MutableLiveData<String>()

    fun getInformationStaff() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getInformationStaff().apply {
                if (isSuccessful) {
                    if (data != null) {

                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}