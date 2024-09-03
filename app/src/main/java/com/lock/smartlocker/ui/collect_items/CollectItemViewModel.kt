package com.lock.smartlocker.ui.collect_items

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.UpdateInventoryTransactionRequest
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.data.models.LockerStatus
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
    val listLockerInfo = MutableLiveData<List<LockerInfo>?>()
    val transactionId = MutableLiveData<String>()
    val listLockerId = MutableLiveData<List<String>>()

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
                        listLockerInfo.value?.map { lockerInfo ->
                            val matchingStatus = data.locker_list.find { it.lockerId == lockerInfo.lockerId }
                            if (matchingStatus != null) {
                                lockerInfo.doorStatus = matchingStatus.doorStatus
                            }
                        }
                        checkStatusDoor(data.locker_list)
                        uiScope.launch { collectItemListener?.sendCommandOpenLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
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
                        listLockerInfo.value?.map { lockerInfo ->
                            val matchingStatus = data.locker_list.find { it.lockerId == lockerInfo.lockerId }
                            if (matchingStatus != null) {
                                lockerInfo.doorStatus = matchingStatus.doorStatus
                            }
                        }
                        checkStatusDoor(data.locker_list)
                        uiScope.launch { collectItemListener?.sendCommandOpenLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun checkStatusDoor(lockerStatus: ArrayList<LockerStatus>){
        val allNotOpen = lockerStatus.all { it.doorStatus == 1 || it.doorStatus == -1 }
        val allOpen = lockerStatus.all { it.doorStatus == 0 }
        if (allNotOpen) {
            mStatusText.postValue(R.string.error_all_doors_not_open)
        }else if (allOpen.not()) {
            mStatusText.postValue(R.string.error_some_doors_not_open)
        }
        if (allOpen) {
            showStatusText.postValue(false)
        }
    }

    fun updateInventoryTransaction() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = UpdateInventoryTransactionRequest()
            param.transaction_id = transactionId.value
            param.type_update = 0
            param.serial_numbers = listLockerInfo.value?.map { it.serialNumber }
            loanRepository.updateInventoryTransaction(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        collectItemListener?.updateInventorySuccess()
                        PreferenceHelper.writeString(ConstantUtils.LOCKER_INFOS, Gson().toJson(listLockerInfo.value))
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}