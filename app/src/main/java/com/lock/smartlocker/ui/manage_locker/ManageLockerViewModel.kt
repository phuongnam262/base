package com.lock.smartlocker.ui.manage_locker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.DisableLockerRequest
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class ManageLockerViewModel(
    private val managerRepository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {

    var manageLockerListener: ManageLockerListener? = null
    private val _lockers = MutableLiveData<List<Locker>>()
    val lockers: LiveData<List<Locker>> get() = _lockers
    var listLockerId = MutableLiveData<List<String>>()
    var lockerSelectedId: String? = null
    var isDisable: Boolean = false

    fun getListLocker() {
        ioScope.launch {
           mLoading.postValue(true)
            managerRepository.getSetting().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val filteredLockers = data.lockers.filter { it.name != "Console" }.sortedBy { it.name }
                        filteredLockers.map { it.doorStatus = 2 }
                        _lockers.postValue(filteredLockers)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun openAllLocker() {
        ioScope.launch {
            val request = HardwareControllerRequest(
                lockerIds = listLockerId.value,
                userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
                openType = 2
            )
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        _lockers.value?.map { locker ->
                            val matchingStatus =
                                data.locker_list.find { it.lockerId == locker.lockerId }
                            if (matchingStatus != null) {
                                locker.doorStatus = matchingStatus.doorStatus
                            }
                        }
                        _lockers.value?.let { checkStatusDoor(it) }
                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun openLocker(lockerId: String) {
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
                        _lockers.value?.map { locker ->
                            val matchingStatus =
                                data.locker_list.find { it.lockerId == locker.lockerId }
                            if (matchingStatus != null) {
                                locker.doorStatus = matchingStatus.doorStatus
                                if (matchingStatus.doorStatus == 1 || matchingStatus.doorStatus == -1) {
                                    mStatusText.postValue(R.string.error_open_failed)
                                }else{
                                    showStatusText.postValue(false)
                                }
                            }
                        }
                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun checkStatusDoor(lockerStatus: List<Locker>?) {
        val allNotOpen = lockerStatus?.all { it.doorStatus == 1 || it.doorStatus == -1 }
        val allOpen = lockerStatus?.all { it.doorStatus == 0 }
        if (allNotOpen == true) {
            mStatusText.postValue(R.string.error_all_doors_not_open)
        } else if (allOpen == false) {
            mStatusText.postValue(R.string.error_some_doors_not_open)
        }
        if (allOpen == true) {
            showStatusText.postValue(false)
        }
    }

    fun checkDoorStatus() {
        ioScope.launch {
            mLoading.postValue(true)
            showStatusText.postValue(false)
            val request = HardwareControllerRequest(
                lockerIds = listLockerId.value,
                userHandler = PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
                openType = 2
            )
            hardwareControllerRepository.checkMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        _lockers.value?.map { locker ->
                            val matchingStatus =
                                data.locker_list.find { it.lockerId == locker.lockerId }
                            if (matchingStatus != null) {
                                locker.doorStatus = matchingStatus.doorStatus
                            }
                        }
                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun disableLocker() {
        ioScope.launch {
            val request = DisableLockerRequest(
                locker_id = lockerSelectedId,
                is_disable = isDisable
            )
            mLoading.postValue(true)
            managerRepository.disableLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        _lockers.value?.map { locker ->
                            if (locker.lockerId == data.locker.lockerId) {
                                locker.lockerStatus = data.locker.lockerStatus
                                locker.lockerAction = data.locker.lockerAction
                            }
                        }
                        uiScope.launch { manageLockerListener?.openLockerSuccess() }
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}