package com.lock.smartlocker.ui.inputemail

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class InputEmailViewModel(
    private val managerRepository: ManagerRepository,
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {

    var inputEmailListener: InputEmailListener? = null
    var typeOpen : String? = null
    val email = MutableLiveData<String>()
    val subEmail = MutableLiveData<String>()

    fun consumerLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            if (email.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_email_empty)
                inputEmailListener?.consumerLoginFail("","")
                return@launch
            }else{
                if (typeOpen == null) {
                    val getUser = userLockerRepository.checkEmail(email.value + subEmail.value)
                    if (getUser != null) {
                        mStatusText.postValue(R.string.error_email_exited)
                        inputEmailListener?.consumerLoginFail("","")
                        return@launch
                    } else showStatusText.postValue(false)
                }else showStatusText.postValue(false)
            }
            val param = ConsumerLoginRequest()
            param.email = email.value + subEmail.value
            managerRepository.consumerLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        inputEmailListener?.consumerLoginSuccess(param.email)
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.token)
                        PreferenceHelper.writeString(ConstantUtils.USER_NAME, param.email)
                        PreferenceHelper.writeString(ConstantUtils.USER_AVATAR, "")
                        showStatusText.postValue(false)
                    }
                }else {
                    PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, "")
                    PreferenceHelper.writeString(ConstantUtils.USER_NAME, "")
                    if (status != ConstantUtils.REQUIRE_OTP) {
                        handleError(status)
                        inputEmailListener?.consumerLoginFail("","")
                    }
                    else inputEmailListener?.consumerLoginFail(param.email, status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}