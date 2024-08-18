package com.lock.smartlocker.ui.select_available_locker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class SelectAvailableLockerViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var selectAvailableListener: SelectAvailableLockerListener? = null

    private val _lockers = MutableLiveData<List<Locker>>()
    val lockers: LiveData<List<Locker>> get() = _lockers

    private val _selectedLocker = MutableLiveData<Locker?>()
    val selectedLocker: LiveData<Locker?> get() = _selectedLocker

    init {
        loadListAvailableLockers()
    }

    fun selectLocker(locker: Locker) {
        _selectedLocker.value = locker
    }

    fun loadListAvailableLockers() {
        ioScope.launch {
            returnRepository.listReturnAvailableLockers().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val availableLockerIds = data.locker_available

                        val jsonSetting = PreferenceHelper.getString(ConstantUtils.GET_SETTING, "")
                        if (jsonSetting.isNotEmpty()) {
                            val settingResponseType = object : TypeToken<GetSettingResponse>() {}.type
                            val settingsResponse: GetSettingResponse = Gson().fromJson(jsonSetting, settingResponseType)

                            val allLockers = settingsResponse.lockers

                            // Lọc các locker có sẵn từ danh sách tất cả lockers
                            val availableLockers = allLockers.filter { it.lockerId in availableLockerIds }

                            _lockers.postValue(availableLockers)

                            // Gọi listener nếu cần
//                            selectAvailableListener?.onLoadAvailableLockersSuccess(availableLockers)
                        } else {
                            // Xử lý trường hợp không có cài đặt
                            _lockers.postValue(emptyList())
//                            selectAvailableListener?.onLoadAvailableLockersFailed("No settings available.")
                        }

                    }
                }else handleError(status)
            }
        }
    }

    fun openLocker() {
        // call api open locker với lockerId
        println(selectedLocker.value?.lockerId)

    }
}

