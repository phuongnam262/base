package com.lock.smartlocker.ui.splash

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class SplashViewModel(
    private val startAppRepository: StartAppRepository
) : BaseViewModel() {
    var splashListener: SplashListener? = null
    val allSuccess = MutableLiveData<Boolean>()

    fun terminalLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            startAppRepository.terminalLogin().apply {
                if (isSuccessful) {
                    if (data != null) {
                        PreferenceHelper.writeString(ConstantUtils.API_TOKEN, data.api_token)
                        PreferenceHelper.writeString(ConstantUtils.TERMINAL_NAME, data.terminal.name)
                        PreferenceHelper.writeString(ConstantUtils.OWNER_TEXT, data.setting.kiosk.kioskConfig.ownerText)
                        PreferenceHelper.writeString(ConstantUtils.BACKGROUND, data.setting.kiosk.kioskConfig.homeScreenBackground)
                        PreferenceHelper.writeBoolean(ConstantUtils.CHECK_DOOR_STATUS_ON_CONFIRM, data.setting.kiosk.kioskConfig.checkDoorStatusOnConfirm)
                        PreferenceHelper.writeInt(ConstantUtils.AUTO_TRIGGER_DELAY, data.setting.kiosk.kioskConfig.autoTriggerDelay)
                        PreferenceHelper.writeString(ConstantUtils.EMAIL_DOMAIN, data.setting.kiosk.kioskConfig.emailDomain)
                        PreferenceHelper.writeBoolean(ConstantUtils.ENABLE_2FA, data.setting.systemSetting.enable2FA)
                        PreferenceHelper.writeString(ConstantUtils.ORDER_CATEGORY, data.setting.kiosk.kioskConfig.orderCategoryOnKiosk)
                        PreferenceHelper.writeString(ConstantUtils.ORDER_MODEL, data.setting.kiosk.kioskConfig.orderModelOnKiosk)
                        PreferenceHelper.writeString(ConstantUtils.TERMINAL_LOGIN, Gson().toJson(data))
                        getSetting()
                        splashListener?.updateLayout()
                    }
                }else handleError(status)
            }
        }
    }

    private fun getSetting() {
        ioScope.launch {
            startAppRepository.getSetting().apply {
                if (isSuccessful) {
                    if (data != null) {
                        data.lockers.map { it.doorStatus = 2 }
                        PreferenceHelper.writeString(ConstantUtils.GET_SETTING, Gson().toJson(data))
                        getListCategory()
                    }
                }else handleError(status)
            }
        }
    }

    private fun getListCategory() {
        ioScope.launch {
            startAppRepository.listCategory().apply {
                if (isSuccessful) {
                    if (data != null) {
                        if (PreferenceHelper.getString(ConstantUtils.ORDER_CATEGORY) == "position"){
                            PreferenceHelper.writeString(ConstantUtils.LIST_CATEGORY, Gson().toJson(data.categories.sortedBy { it.position }))
                        }else{
                            PreferenceHelper.writeString(ConstantUtils.LIST_CATEGORY, Gson().toJson(data.categories.sortWith(compareBy { it.categoryName })))
                        }
                        allSuccess.postValue(true)
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}