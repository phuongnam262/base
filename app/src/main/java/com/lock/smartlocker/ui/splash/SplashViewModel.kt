package com.lock.smartlocker.ui.splash

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
                    mLoading.postValue(false)
                    if (data != null) {
                        PreferenceHelper.writeString(ConstantUtils.LIST_CATEGORY, Gson().toJson(data))
                        allSuccess.postValue(true)
                    }
                }else handleError(status)
            }
        }
    }
}