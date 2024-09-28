package com.lock.smartlocker.ui.recognize_face

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.request.EndUserLoginRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.facedetector.preference.PreferenceUtils
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch

class RecognizeFaceViewModel(
    private val userFaceRepository: UserFaceRepository,
    private val managerRepository: ManagerRepository
) : BaseViewModel() {
    var recognizeFaceListener: RecognizeFaceListener? = null
    var typeOpen : String? = null
    var email : String? = null
    var cardNumber : String? = null

    fun detectImage(strBase64: String) {
        ioScope.launch {
            mLoading.postValue(true)
            try {
                val imageBase64Request = ImageBase64Request(strBase64)
                val getDetectResponse = userFaceRepository.detectImage(imageBase64Request)
                getDetectResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getDetectResponse.result.liveness == 1) {
                            searchPerson(strBase64)
                            return@launch
                        }else handelFaceFail(getDetectResponse)
                    } else {
                        handelFaceFail(getDetectResponse)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun searchPerson(strBase64: String) {
        ioScope.launch {
            mLoading.postValue(true)
            try {
                val imageSearchRequest = ImageSearchRequest(strBase64)
                val getSearchResponse = userFaceRepository.searchPerson(imageSearchRequest)
                getSearchResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getSearchResponse.result != null) {
                            if (getSearchResponse.result.similar < 0.7) {
                                mStatusText.postValue(R.string.error_no_face_detect)
                                recognizeFaceListener?.faceExited()
                            } else {
                                getSearchResponse.result.personCode?.let { it1 ->
                                    getUserLocker(it1)
                                    PreferenceHelper.writeString(ConstantUtils.USER_AVATAR, strBase64)
                                }
                            }
                        } else {
                            mStatusText.postValue(R.string.error_no_face_detect)
                            recognizeFaceListener?.faceExited()
                        }
                        return@launch
                    } else {
                        statusText.postValue(getSearchResponse.message)
                        showStatusText.postValue(true)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun handelFaceFail(detectImageResponse: DetectImageResponse) {
        if (detectImageResponse.message.contains("not found") || detectImageResponse.result.liveness != 1) {
            uiScope.launch {
                recognizeFaceListener?.faceNotFound()
                mStatusText.postValue(R.string.face_not_found)
            }
        }else{
            mOtherError.postValue(detectImageResponse.message)
        }
    }

    private fun getUserLocker(personCode: String) {
        ioScope.launch {
            mLoading.postValue(true)
            val getUser = userFaceRepository.getUserLocker(personCode)
            if (getUser?.id != null) {
                PreferenceHelper.writeString(ConstantUtils.ADMIN_NAME,
                    getUser.personName?.ifEmpty { getUser.email })
                email = getUser.email
                cardNumber = getUser.cardNumber
                if (typeOpen == ConstantUtils.TYPE_CONSUMABLE_COLLECT) {
                    if (cardNumber.isNullOrEmpty()){
                        uiScope.launch {
                            recognizeFaceListener?.faceNotFound()
                            mStatusText.postValue(R.string.work_card_error)
                            showStatusText.postValue(true)
                        }
                    }else {
                        showButtonUsingMail.postValue(false)
                        showStatusText.postValue(true)
                        uiScope.launch {
                            recognizeFaceListener?.handleSuccess(getUser.personName?.ifEmpty { getUser.email })
                        }
                    }
                } else {
                    showButtonUsingMail.postValue(false)
                    showStatusText.postValue(true)
                    uiScope.launch {
                        recognizeFaceListener?.handleSuccess(getUser.personName?.ifEmpty { getUser.email })
                    }
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun consumerLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = ConsumerLoginRequest()
            param.email = email
            managerRepository.consumerLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.token)
                        recognizeFaceListener?.consumerLoginSuccess(email)
                    }
                }else {
                    if (status != ConstantUtils.REQUIRE_OTP) handleError(status)
                    else recognizeFaceListener?.consumerLoginFail(param.email, status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun endUserLogin() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = EndUserLoginRequest(card_number = cardNumber)
            managerRepository.endUserLogin(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                        PreferenceHelper.writeString(ConstantUtils.USER_TOKEN, data.endUser.userToken)
                        recognizeFaceListener?.consumerLoginSuccess(data.endUser.fullName)
                    }
                }else {
                    if (status != ConstantUtils.REQUIRE_OTP) handleError(status)
                    else recognizeFaceListener?.consumerLoginFail(data?.endUser?.fullName, status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}