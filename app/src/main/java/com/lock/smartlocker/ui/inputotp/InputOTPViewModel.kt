package com.lock.smartlocker.ui.inputotp

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.ui.base.BaseViewModel

class InputOTPViewModel(
    private val startAppRepository: StartAppRepository
) : BaseViewModel() {

    val otpText = MutableLiveData<String>()
    val otpEmail = MutableLiveData<String>()

    fun sendOTP() {

    }

    fun onResendClicked() {

    }
}