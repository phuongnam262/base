package com.lock.smartlocker.ui.collect_items

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class CollectItemViewModel(
    private val loanRepository: LoanRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {

    var collectItemListener: CollectItemListener? = null
    val listLockerInfo = MutableLiveData<List<LockerInfo>>()
    val transactionId = MutableLiveData<String>()
    val listLockerId = MutableLiveData<List<String>>()
    val doorStatus = MutableLiveData<Int?>()

    fun openLocker() {
        val request = HardwareControllerRequest(
            lockerIds = listLockerId.value,
            userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
            openType = 2
        )
        ioScope.launch {
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        collectItemListener?.sendCommandOpenLockerSuccess()
                        mLoading.postValue(false)
                    }
                } else handleError(status)
            }
        }
    }

    fun reopenLocker(lockerId: String) {
        val request = HardwareControllerRequest(
            lockerIds = listOf(lockerId),
            userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
            openType = 2
        )
        ioScope.launch {
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        doorStatus.postValue(data.locker_list.first().doorStatus)
                        mLoading.postValue(false)
                    }
                } else handleError(status)
            }
        }
    }
}