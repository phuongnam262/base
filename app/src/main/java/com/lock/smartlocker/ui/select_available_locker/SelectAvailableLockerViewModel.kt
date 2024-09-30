package com.lock.smartlocker.ui.select_available_locker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class SelectAvailableLockerViewModel(
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {
    var selectAvailableListener: SelectAvailableLockerListener? = null

    private val _lockers = MutableLiveData<List<Locker>>()
    val lockers: LiveData<List<Locker>> get() = _lockers

    private val _selectedLocker = MutableLiveData<Locker?>()
    val selectedLocker: LiveData<Locker?> get() = _selectedLocker

    val typeInput = MutableLiveData<String?>()

    var isReturnFlow = true

    fun selectLocker(locker: Locker) {
        _selectedLocker.value = locker
        enableButtonProcess.postValue(true)
    }

    fun loadListAvailableLockers() {
        ioScope.launch {
            val availableLockerIds = PreferenceHelper.getString(ConstantUtils.RETURN_AVAILABLE_LOCKER_LIST, "")
            val jsonSetting = PreferenceHelper.getString(ConstantUtils.GET_SETTING, "")
            if (jsonSetting.isNotEmpty()) {
                val settingResponseType = object : TypeToken<GetSettingResponse>() {}.type
                val settingsResponse: GetSettingResponse = Gson().fromJson(jsonSetting, settingResponseType)
                val allLockers = settingsResponse.lockers
                val availableLockers = allLockers.filter { it.lockerId in availableLockerIds }
                _lockers.postValue(availableLockers)
            } else {
                _lockers.postValue(emptyList())
            }
        }
    }

    fun openLocker() {
        val lockerId = selectedLocker.value?.lockerId ?: return
        val request = HardwareControllerRequest(
            lockerIds = listOf(lockerId),
            userHandler = if(isReturnFlow) PreferenceHelper.getString(ConstantUtils.USER_NAME, "User")
            else PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin"),
            openType = 2
        )
        ioScope.launch {
            mLoading.postValue(true)
            hardwareControllerRepository.openMassLocker(request).apply {
                if (isSuccessful) {
                    if (data != null) {
                        selectAvailableListener?.sendCommandOpenLockerSuccess()
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}

