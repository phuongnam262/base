package com.lock.smartlocker.ui.scan_item

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.entities.request.UpdateInventoryTransactionRequest
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ScanItemViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {

    var scanItemListener: ScanItemListener? = null
    val listLockerInfo = MutableLiveData<List<LockerInfo>>()
    val transactionId = MutableLiveData<String>()
    val listLockerId = MutableLiveData<List<String>>()
    val doorStatus = MutableLiveData<Int?>()

    fun updateInventoryTransaction() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = UpdateInventoryTransactionRequest()
            param.transaction_id = transactionId.value
            param.type_update = 1
            param.serial_numbers = listLockerInfo.value
                ?.filter { it.scanValue == 0 }
                ?.map { it.serialNumber }
            param.serial_skips = listLockerInfo.value
                ?.filter { it.scanValue == 1 }
                ?.map { it.serialNumber }
            param.serial_not_founds = listLockerInfo.value
                ?.filter { it.scanValue == 2 }
                ?.map { it.serialNumber }
            loanRepository.updateInventoryTransaction(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        scanItemListener?.updateInventorySuccess()
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun scanningItem(serialNumber: String) {
        listLockerInfo.value = listLockerInfo.value?.map {
            if (it.serialNumber == serialNumber) {
                it.scanValue = 1
            }
            it
        }
    }
}