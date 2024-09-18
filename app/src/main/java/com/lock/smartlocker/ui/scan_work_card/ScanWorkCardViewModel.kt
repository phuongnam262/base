package com.lock.smartlocker.ui.scan_work_card

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.CheckCardRequest
import com.lock.smartlocker.data.entities.request.EndUserLoginRequest
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class ScanWorkCardViewModel(
    private val managerRepository: ManagerRepository,
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {

    var scanCardListener: ScanCardListener? = null
    val workCardText = MutableLiveData<String>()
    var typeOpen : String? = null

    fun checkCardNumber() {
        ioScope.launch {
            mLoading.postValue(true)
            if (workCardText.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_card_emplty)
                return@launch
            }else{
                if (typeOpen != ConstantUtils.TYPE_CONSUMABLE_COLLECT) {
                    val getUser = userLockerRepository.checkCardNumber(workCardText.value!!.lowercase())
                    if (getUser != null) {
                        mStatusText.postValue(R.string.error_card_exited)
                        return@launch
                    } else showStatusText.postValue(false)
                }else showStatusText.postValue(false)
            }
            val param = CheckCardRequest()
            param.cardNumber = workCardText.value!!.trim()
            managerRepository.checkCardNumber(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        showStatusText.postValue(false)
                        scanCardListener?.handleSuccess(data.endUser.fullName, workCardText.value!!.trim())
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun endUserLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            if (workCardText.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_card_emplty)
                return@launch
            }else showStatusText.postValue(false)

            val param = EndUserLoginRequest(card_number = workCardText.value)
            managerRepository.endUserLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.endUser.userToken)
                        scanCardListener?.handleSuccess(data.endUser.fullName, data.endUser.cardNumber)
                    }
                }else {
                    handleError(status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}