package com.lock.smartlocker.ui.admin_login

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.Coroutines
import kotlinx.coroutines.launch

class AdminLoginViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {
    var showPassword = MutableLiveData<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var adminLoginListener : AdminLoginListener? = null

    fun clickShowPassword() {
        showPassword.value = showPassword.value != true
    }

    fun adminLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            if (username.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_username_empty)
                isErrorText.postValue(true)
                return@launch
            }
            if (password.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_password_empty)
                isErrorText.postValue(true)
                return@launch
            }
            val param = AminLoginRequest()
            param.user_name = username.value
            param.password = password.value
            managerRepository.adminLogin(param).apply {
                if (isSuccessful) {

                    if (data != null) {
                        adminLoginListener?.adminLoginSuccess(data)
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.staff.userToken)
                        showStatusText.postValue(false)
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}