package com.lock.smartlocker.ui.collect_consumable_item

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConfirmConsumableCollectRequest
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.ReportConsumableRequest
import com.lock.smartlocker.data.entities.request.ReportConsumableTransactionRequest
import com.lock.smartlocker.data.models.ConfirmCollectItem
import com.lock.smartlocker.data.models.LockerInfoCollect
import com.lock.smartlocker.data.models.LockerStatus
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class CollectConsumableItemViewModel(
    private val loanRepository: LoanRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {

    var collectItemListener: CollectConsumableItemListener? = null
    val listLockerInfo = MutableLiveData<List<LockerInfoCollect>?>()
    val transactionId = MutableLiveData<String>()
    val listLockerId = MutableLiveData<List<String>>()
    var isConfirm = MutableLiveData<Boolean>(false)

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
                                if (matchingStatus.doorStatus == 1 || matchingStatus.doorStatus == -1) {
                                    mStatusText.postValue(R.string.error_open_failed)
                                }else{
                                    showStatusText.postValue(false)
                                }
                            }
                        }
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

    fun confirmCollectConsumable() {
        ioScope.launch {
            mLoading.postValue(true)
            val data = listLockerInfo.value?.map { info ->
                ConfirmCollectItem(
                    lockerId = info.lockerId,
                    consumableId = info.consumableId,
                    categoryId = info.categoryId,
                    quantity = info.takeNumber
                )
            }
            val param = ConfirmConsumableCollectRequest()
            param.transaction_id = transactionId.value
            param.data_infos = data
            loanRepository.confirmCollectConsumable(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        collectItemListener?.confirmCollectSuccess()
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun reportConsumableTransaction(reason: String, lockerId: String, consumableId: String) {
        ioScope.launch {
            mLoading.postValue(true)
            val params = ReportConsumableTransactionRequest(
                lockerId = lockerId,
                consumableId = consumableId,
                transactionId = transactionId.value,
                reason = reason
            )
            loanRepository.reportConsumableTransaction(params).apply {
                if (isSuccessful) {
                    return@launch
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}