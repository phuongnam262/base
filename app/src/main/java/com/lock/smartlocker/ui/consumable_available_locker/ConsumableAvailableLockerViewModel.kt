package com.lock.smartlocker.ui.consumable_available_locker

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class ConsumableAvailableLockerViewModel(
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {
    var consumableAvailableLockerListener: ConsumableAvailableLockerListener? = null

    val lockers = MutableLiveData<List<LockerConsumable>>()

    var selectedLocker = MutableLiveData<LockerConsumable?>()
    fun selectLocker(locker: LockerConsumable) {
        selectedLocker.value = locker
    }

    fun openLocker() {
        val lockerId = selectedLocker.value?.lockerId ?: return
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
                        consumableAvailableLockerListener?.sendCommandOpenLockerSuccess(
                            selectedLocker.value?.lockerId)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}

