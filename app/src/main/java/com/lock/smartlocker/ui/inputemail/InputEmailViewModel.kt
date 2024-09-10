package com.lock.smartlocker.ui.inputemail

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class InputEmailViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    var inputEmailListener: InputEmailListener? = null
    val email = MutableLiveData<String>()
    val subEmail = MutableLiveData<String>()

    fun consumerLogin(typeOpen: String?) {
        ioScope.launch {
            if (email.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_email_empty)
                inputEmailListener?.consumerLoginFail("","")
                return@launch
            }else{
                showStatusText.postValue(false)
            }
            mLoading.postValue(true)
            val param = ConsumerLoginRequest()
            param.email = email.value + subEmail.value
            managerRepository.consumerLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        inputEmailListener?.consumerLoginSuccess(param.email)
                        if (typeOpen != null)  PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.token)
                        showStatusText.postValue(false)
                    }
                }else {
                    if (status != ConstantUtils.REQUIRE_OTP) handleError(status)
                    else inputEmailListener?.consumerLoginFail(param.email, status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}