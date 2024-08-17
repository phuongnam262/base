package com.lock.smartlocker.ui.admin_login

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.ui.base.BaseViewModel

class AdminLoginViewModel(
) : BaseViewModel() {
    var showPassword = MutableLiveData<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun clickShowPassword() {
        showPassword.value = showPassword.value != true
    }

}