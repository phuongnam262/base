package com.lock.smartlocker.ui.recognize_face

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch

class RecognizeFaceViewModel(
    private val userLockerRepository: UserFaceRepository,
    private val managerRepository: ManagerRepository
) : BaseViewModel() {
    var recognizeFaceListener: RecognizeFaceListener? = null
    var email : String? = null

    fun detectImage(strBase64: String) {
        ioScope.launch {
            try {
                val imageBase64Request = ImageBase64Request(strBase64)
                val getDetectResponse = userLockerRepository.detectImage(imageBase64Request)
                getDetectResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getDetectResponse.result.liveness == 1) {
                            searchPerson(strBase64)
                            return@launch
                        }
                    } else {
                        handelFaceFail(getDetectResponse)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }
    }

    private fun searchPerson(strBase64: String) {
        ioScope.launch {
            try {
                val imageSearchRequest = ImageSearchRequest(strBase64)
                val getSearchResponse = userLockerRepository.searchPerson(imageSearchRequest)
                getSearchResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getSearchResponse.result != null) {
                            if (getSearchResponse.result.similar < 0.7) {
                                mStatusText.postValue(R.string.error_no_face_detect)
                                isErrorText.postValue(true)
                                recognizeFaceListener?.faceExited()
                            } else {
                                getSearchResponse.result.personCode?.let { it1 ->
                                    getUserLocker(it1)
                                }
                            }
                        } else {
                            mStatusText.postValue(R.string.error_no_face_detect)
                            isErrorText.postValue(true)
                            recognizeFaceListener?.faceExited()
                        }
                        return@launch
                    } else {
                        statusText.postValue(getSearchResponse.message)
                        showStatusText.postValue(true)
                        isErrorText.postValue(true)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }
    }

    private fun handelFaceFail(detectImageResponse: DetectImageResponse) {
        if (detectImageResponse.message.contains("not found")) {
            uiScope.launch {
                recognizeFaceListener?.faceNotFound()
                mMessage.postValue(R.string.face_not_found)
            }
        }
    }

    private fun getUserLocker(personCode: String) {
        ioScope.launch {
            val getUser = userLockerRepository.getUsedLocker(personCode)
            if (getUser?.id != null) {
                getUser.email?.let {
                    PreferenceHelper.writeString(ConstantUtils.ADMIN_NAME, email)
                    email = it
                    isErrorText.postValue(false)
                    showButtonUsingMail.postValue(false)
                    showStatusText.postValue(true)
                    getUser.personCode?.let { it1 -> recognizeFaceListener?.handleSuccess(it1, it) }
                }
            }
        }
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
                        recognizeFaceListener?.consumerLoginSuccess()
                    }
                }else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}