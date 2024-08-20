package com.lock.smartlocker.ui.deposit_item

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DepositItemViewModel(
    private val returnRepository: ReturnRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {
    var depositItemListener: DepositItemListener? = null

    val doorStatus = MutableLiveData<Int?>()
    val modelName = MutableLiveData<String>()

    fun getModelName(serialNumber: String) {
        ioScope.launch {
            val param = GetItemReturnRequest()
            param.serial_number = serialNumber
            returnRepository.getItemReturn(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        modelName.postValue(data.modelName)
                    }
                } else handleError(status)
            }
        }
    }

    private suspend fun checkStatus(lockerId: String) {
        ioScope.launch {
            val request = HardwareControllerRequest(
                lockerIds = listOf(lockerId),
                userHandler = "Chanh",
                openType = 2
            )
            hardwareControllerRepository.checkMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        doorStatus.postValue(data.locker_list.first().doorStatus)
                    }
                } else handleError(status)
            }
        }
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

    fun handleReturnItemProcess(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            checkStatus(returnItemRequest.locker_id ?: "")
            if (doorStatus.value == 0) {
                println("chanh chanh 0${returnItemRequest}")
                mStatusText.postValue(R.string.error_door_has_not_close)
                isErrorText.postValue(true)
            } else {
                returnItem(returnItemRequest)
            }
        }
    }

    fun initialCheckStatus(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            checkStatus(returnItemRequest.locker_id ?: "")
            if (doorStatus.value == 1) {
                handleError("Door has not been opened successfully.")
            }
        }
    }

    private fun returnItem(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            println("chanh:: $returnItemRequest")
            returnRepository.returnItem(returnItemRequest).apply {
                if (isSuccessful) {
                    if (data != null) {
                        depositItemListener?.returnItemSuccess()
                    }
                }else handleError(status)
            }
        }
    }

     fun reopenLocker(lockerId: String) {
        val request = HardwareControllerRequest(
            lockerIds = listOf(lockerId),
            userHandler = "Chanh",
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