package com.lock.smartlocker.ui.admin_dashboard

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class AdminDashboardViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    val numberLockerAvailable = MutableLiveData<Int>()
    val numberItemFaulty = MutableLiveData<Int>()

    fun getInformationStaff() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getInformationStaff().apply {
                if (isSuccessful) {
                    if (data != null) {
                        numberLockerAvailable.postValue(data.lockerAvailable.size)
                        numberItemFaulty.postValue(data.itemFaulty)
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}