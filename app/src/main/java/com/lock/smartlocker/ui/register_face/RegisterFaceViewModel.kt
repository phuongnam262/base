package com.lock.smartlocker.ui.register_face

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.UserLockerModel
import com.lock.smartlocker.data.entities.request.AddPersonRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch
import kotlin.random.Random

class RegisterFaceViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    var registerFaceListener: RegisterFaceListener? = null
    val emailRegister = MutableLiveData<String>()
    fun detectImage(strBase64: String) {
        ioScope.launch {
            mLoading.postValue(true)
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
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun searchPerson(strBase64: String) {
        ioScope.launch {
            mLoading.postValue(true)
            try {
                val imageSearchRequest = ImageSearchRequest(strBase64)
                val getSearchResponse = userLockerRepository.searchPerson(imageSearchRequest)
                getSearchResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getSearchResponse.result != null) {
                            if (getSearchResponse.result.similar < 0.7) {
                                addPerson(strBase64)
                            } else {
                                mStatusText.postValue(R.string.face_exits)
                                registerFaceListener?.faceExited()
                            }
                        } else {
                            addPerson(strBase64)
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

    private fun addPerson(strBase64: String) {
        ioScope.launch {
            mLoading.postValue(true)
            try {
                val personCodeGen = generateRandomNumber()
                val addPersonModel =
                    AddPersonRequest("1", personCodeGen, emailRegister.value, strBase64, 1)
                val getResponse = userLockerRepository.addPerson(addPersonModel)
                getResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        val userLockerModel =
                            UserLockerModel(
                                0,
                                emailRegister.value,
                                addPersonModel.personCode,
                                addPersonModel.personGroup,
                                0,
                                emailRegister.value,
                                0
                            )
                        val saveUser = userLockerRepository.saveUser(userLockerModel)
                        if (saveUser > 0) {
                            userLockerModel.email?.let { it1 ->
                                uiScope.launch { registerFaceListener?.handleSuccess(
                                    personCodeGen,
                                    it1
                                ) }
                            }
                        } else {
                            mMessage.postValue(R.string.save_user_fail)
                        }
                        return@launch
                    } else {
                        mMessage.postValue(R.string.save_user_fail)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun generateRandomNumber(): String {
        val randomNumber = Random.nextInt(100000, 999999)
        return randomNumber.toString()
    }

    private fun handelFaceFail(detectImageResponse: DetectImageResponse) {
        if (detectImageResponse.message.contains("not found")) {
            uiScope.launch {
                registerFaceListener?.faceNotFound()
                mMessage.postValue(R.string.face_not_found)
            }
        }else{
            mOtherError.postValue(detectImageResponse.message)
        }
    }
}