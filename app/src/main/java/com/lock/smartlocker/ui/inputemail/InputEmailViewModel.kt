package com.lock.smartlocker.ui.inputemail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class InputEmailViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {

    var inputEmailListener: InputEmailListener? = null
    val email = MutableLiveData<String>()
    val subEmail = MutableLiveData<String>()

    private val _spinnerItems = MutableLiveData<List<String>>()
    val spinnerItems: LiveData<List<String>> = _spinnerItems

    fun consumerLogin() {
        ioScope.launch {
            if (email.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_email_empty)
                isErrorText.postValue(true)
                return@launch
            }
            val param = ConsumerLoginRequest()
            param.email = email.value + subEmail.value
            managerRepository.consumerLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        inputEmailListener?.consumerLoginSuccess()
                        showStatusText.postValue(false)
                    }
                }else handleError(status)
            }
        }
    }
    fun loadSpinnerItems() {
        val items = listOf("@gmail.com", "@yahoo.com", "@hotmail.com", "@edu")
        _spinnerItems.value = items
    }
}