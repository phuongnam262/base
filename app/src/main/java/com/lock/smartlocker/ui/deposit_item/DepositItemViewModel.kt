package com.lock.smartlocker.ui.deposit_item

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
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
    val modelName = MutableLiveData<String>()
    val typeInput = MutableLiveData<String?>()

    private suspend fun checkStatus(lockerId: String) {
        ioScope.launch {
            mLoading.postValue(true)
            val request = HardwareControllerRequest(
                lockerIds = listOf(lockerId),
                userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
                openType = 2
            )
            hardwareControllerRepository.checkMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        doorStatus.postValue(data.locker_list.first().doorStatus)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

//    private fun getCheckDoorStatusOnConfirm(): Boolean {
//        return PreferenceHelper.getBoolean(ConstantUtils.CHECK_DOOR_STATUS_ON_CONFIRM)
//    }
//
//    private fun getMandatoryDoorClosureVerification(): Boolean {
//        return PreferenceHelper.getBoolean(ConstantUtils.MANDATORY_DOOR_CLOSURE_VERIFICATION)
//    }

//    private fun startAutoConfirmAfterDelay(returnItemRequest: ReturnItemRequest) {
//        ioScope.launch {
//            handleError("The door is closed. Auto-confirm will be triggered in 10 seconds.")
//            delay(10000)
//            returnItem(returnItemRequest)
//        }
//    }

//    private fun startCheckingDoorStatus(returnItemRequest: ReturnItemRequest) {
//        ioScope.launch {
//            isWaitingForDoorClosure.postValue(true)
//            while (isWaitingForDoorClosure.value == true) {
//                checkStatus(returnItemRequest.locker_id ?: "")
//                if (doorStatus.value == 1) { // Nếu cửa đã đóng
//                    isWaitingForDoorClosure.postValue(false)
//                    startAutoConfirmAfterDelay(returnItemRequest)
//                }
//                delay(3000) // Đợi 3 giây
//            }
//        }
//    }

//    fun handleReturnItemProcess(returnItemRequest: ReturnItemRequest) {
//        val checkDoorStatusOnConfirm = getCheckDoorStatusOnConfirm()
//        val mandatoryDoorClosureVerification = getMandatoryDoorClosureVerification()
//
//        if (checkDoorStatusOnConfirm) {
//            if (doorStatus.value == 1) { // Nếu cửa đã đóng
//                returnItem(returnItemRequest)
//            } else { // Nếu cửa chưa đóng
//                if (mandatoryDoorClosureVerification) {
//                    handleError("Door has not been closed.")
//                    startCheckingDoorStatus(returnItemRequest)
//                } else {
//                    handleError("Door has not been closed. Tap CONFIRM to continue.")
//                    returnItem(returnItemRequest)
//                }
//            }
//        } else {
//            returnItem(returnItemRequest)
//        }
//    }

    fun handleReturnItemProcess(itemReturn: ItemReturn) {
        ioScope.launch {
//            checkStatus(returnItemRequest.locker_id ?: "")
//            if (doorStatus.value == 0) {
//                println("chanh chanh 0${returnItemRequest}")
//                mStatusText.postValue(R.string.error_door_has_not_close)
//                isErrorText.postValue(true)
//            } else {
                val returnItemRequest = ReturnItemRequest(
                    serial_number = itemReturn.serialNumber,
                    locker_id = itemReturn.lockerId,
                    reason_faulty = itemReturn.reasonFaulty
                )
                if (typeInput.value == ConstantUtils.TYPE_RETURN) returnItem(returnItemRequest)
                else topupItem(returnItemRequest)
            //}
        }
    }

    fun initialCheckStatus(returnItemRequest: ItemReturn) {
        ioScope.launch {
            checkStatus(returnItemRequest.lockerId ?: "")
            if (doorStatus.value == 1) {
                handleError("Door has not been opened successfully.")
            }
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
                        mLoading.postValue(false)
                    }
                } else handleError(status)
            }
        }
    }
}