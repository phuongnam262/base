package com.lock.smartlocker.ui.deposit_consumable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.GetConsumableInLockerRequest
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.models.ConsumableInLocker
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class DepositConsumableViewModel(
    private val managerRepository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {
    var depositConsumableListener: DepositConsumableListener? = null
    private var doorStatus = 0
    private var lockerID: String? = null
    private val _listConsumable = MutableLiveData<List<ConsumableInLocker>>()
    val listConsumable: LiveData<List<ConsumableInLocker>> get() = _listConsumable
    var lockerConsumable = MutableLiveData<LockerConsumable>()

    fun checkLockerStatus(lockerId: String) {
        lockerID = lockerId
        getConsumablesInLocker()
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
                        doorStatus = data.locker_list.first().doorStatus
                        if (doorStatus != 0) {
                            mStatusText.postValue(R.string.error_open_failed)
                        }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun getConsumablesInLocker() {
        ioScope.launch {
            mLoading.postValue(true)
            val params = GetConsumableInLockerRequest()
            params.locker_id = lockerID
            managerRepository.getConsumableInLocker(params).apply {
                if (isSuccessful) {
                    if (data != null) {
                        val locker = LockerConsumable(
                            lockerId = data.lockerId,
                            lockerName = data.lockerName,
                            setPoint = 999,
                            currentQuantity = 999,
                            lockerSize = "",
                            arrowPosition = data.arrowPosition,
                            doorStatus = doorStatus
                        )
                        lockerConsumable.postValue(locker)
                        _listConsumable.postValue(data.consumables)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun topupItem(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            mLoading.postValue(true)
//            managerRepository.topupItem(returnItemRequest).apply {
//                if (isSuccessful) {
//                    if (data != null) {
//                        depositConsumableListener?.topupItemSuccess()
//                    }
//                } else handleError(status)
//            }
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
                        doorStatus = data.locker_list.first().doorStatus
                        lockerConsumable.value.let {
                            if (it != null) {
                                it.doorStatus = doorStatus
                            }
                        }
                        if (doorStatus != 0) {
                            mStatusText.postValue(R.string.error_open_failed)
                        } else showStatusText.postValue(false)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}