package com.lock.smartlocker.ui.deposit_item

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class DepositItemViewModel(
    private val returnRepository: ReturnRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {
    var depositItemListener: DepositItemListener? = null
    val doorStatus = MutableLiveData<Int?>()
    val autoCheckDoorStatus = MutableLiveData<Int?>()
    val modelName = MutableLiveData<String>()
    val lockerName = MutableLiveData<String>()
    val arrowPosition = MutableLiveData<Int?>()
    var returnItem: ItemReturn? = null
    var isReturnFlow = true
    private val handler = Handler(Looper.getMainLooper())
    val isRequireCloseDoor = PreferenceHelper.getBoolean(ConstantUtils.CHECK_DOOR_STATUS_ON_CONFIRM, false)

    private val checkStatusRunnable = object : Runnable {
        override fun run() {
            returnItem?.lockerId?.let { checkStatusCloseOrNot(it) }
            if (doorStatus.value != 1) {
                handler.postDelayed(this, 3000)
            }
        }
    }

    private val autoConfirmRunnable = Runnable {
        showStatusText.postValue(false)
        handleReturnItemProcess(returnItem!!)
    }

    fun startCheckingStatus() {
        handler.post(checkStatusRunnable)
    }

    fun stopChecking() {
        handler.removeCallbacks(checkStatusRunnable)
        handler.removeCallbacks(autoConfirmRunnable)
    }

    private fun startAutoConfirm() {
        handler.postDelayed(autoConfirmRunnable, PreferenceHelper.getInt(ConstantUtils.AUTO_TRIGGER_DELAY, 10) * 1000L)
    }

    fun checkStatusCloseOrNot(lockerId: String) {
        ioScope.launch {
            val request = HardwareControllerRequest(
                lockerIds = listOf(lockerId),
                userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
                openType = 2
            )
            hardwareControllerRepository.checkMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        autoCheckDoorStatus.postValue(data.locker_list.first().doorStatus)
                        if (data.locker_list.first().doorStatus == 0) {
                            mStatusText.postValue(R.string.error_door_has_not_close)
                        }else{
                            uiScope.launch { enableButtonProcess.value = true}
                            handler.removeCallbacks(checkStatusRunnable)
                            startAutoConfirm()
                            mStatusText.postValue(R.string.door_is_closed)
                        }
                    }
                } else handleError(status)
            }
        }
    }

    fun handleReturnItemProcess(itemReturn: ItemReturn) {
        handler.removeCallbacks(autoConfirmRunnable)
        ioScope.launch {
            val returnItemRequest = ReturnItemRequest(
                serial_number = itemReturn.serialNumber,
                locker_id = itemReturn.lockerId,
                reason_faulty = itemReturn.reasonFaulty,
                is_faulty = itemReturn.reasonFaulty.isNotEmpty()
            )
            if (isReturnFlow) returnItem(returnItemRequest)
            else topupItem(returnItemRequest)
        }
    }

    private fun returnItem(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            mLoading.postValue(true)
            returnRepository.returnItem(returnItemRequest).apply {
                if (isSuccessful) {
                    if (data != null) {
                        depositItemListener?.returnItemSuccess()
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun topupItem(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            mLoading.postValue(true)
            returnRepository.topupItem(returnItemRequest).apply {
                if (isSuccessful) {
                    if (data != null) {
                        depositItemListener?.topupItemSuccess()
                    }
                }else handleError(status)
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
                        doorStatus.postValue(data.locker_list.first().doorStatus)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}