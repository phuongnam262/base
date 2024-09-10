package com.lock.smartlocker.ui.inputotp

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.VerifyOTPRequest
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class InputOTPViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    var inputOTPListener: InputOTPListener? = null
    var otpText = MutableLiveData<String>()
    var email = MutableLiveData<String>()

    fun onSendOTP() {
        ioScope.launch {
            if (otpText.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_otp_emplty)
                return@launch
            }
            mLoading.postValue(true)
            val param = VerifyOTPRequest()
            param.email = email.value
            param.otp = otpText.value
            managerRepository.verifyOTP(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.token)
                        showStatusText.postValue(false)
                        inputOTPListener?.verifySuccess(email.value)
                    }
                } else {
                    handleError(status)
                    inputOTPListener?.verifyFail()
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onResend() {

    }
}