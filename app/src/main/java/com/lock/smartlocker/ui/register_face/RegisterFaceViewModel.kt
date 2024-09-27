package com.lock.smartlocker.ui.register_face

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.AddPersonRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.models.UserLockerModel
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch
import kotlin.random.Random

class RegisterFaceViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null
    var registerFaceListener: RegisterFaceListener? = null
    val emailRegister = MutableLiveData<String>()
    val cardNumberRegister = MutableLiveData<String>()
    val nameEndUser = MutableLiveData<String>()
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
                val getSearchResponse = userLockerRepository.searchPerson(imageSearchRequest)
                getSearchResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getSearchResponse.result != null) {
                            mOtherError.postValue("Similar : ${getSearchResponse.result.similar}")
                            if (getSearchResponse.result.similar < 0.7) {
                                addPerson(strBase64)
                            } else {
                                getSearchResponse.result.personCode?.let { it1 ->
                                    getUserLocker(it1)
                                }
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
                        val nameUser = if (cardNumberRegister.value !== null) nameEndUser.value else emailRegister.value
                        val userLockerModel =
                            UserLockerModel(
                                0,
                                nameUser,
                                addPersonModel.personCode,
                                addPersonModel.personGroup,
                                0,
                                emailRegister.value?.lowercase(),
                                cardNumberRegister.value?.lowercase(),
                                0,
                                strBase64
                            )
                        val saveUser = userLockerRepository.saveUser(userLockerModel)
                        if (saveUser > 0) {
                                uiScope.launch { registerFaceListener?.handleSuccess(
                                    personCodeGen,
                                    nameUser!!
                                ) }
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
        if (detectImageResponse.message.contains("not found") || detectImageResponse.result.liveness != 1) {
            uiScope.launch {
                registerFaceListener?.faceNotFound()
                mStatusText.postValue(R.string.face_not_found)
            }
        }else{
            mOtherError.postValue(detectImageResponse.message)
        }
    }

    private fun getUserLocker(personCode: String) {
        ioScope.launch {
            mLoading.postValue(true)
            val getUser = userLockerRepository.getUserLocker(personCode)
            if (getUser?.id != null) {
                if (emailRegister.value != null) {
                    if (getUser.email != null) {
                        uiScope.launch {
                            registerFaceListener?.faceExited()
                            mStatusText.postValue(R.string.face_exits)
                        }
                        return@launch
                    }else if (getUser.cardNumber != null){
                        getUser.email = emailRegister.value?.lowercase()
                        updateUserLocker(getUser)
                        uiScope.launch {
                            registerFaceListener?.faceExited()
                            statusText.postValue(context?.getString(R.string.face_exits_work_card, getUser.cardNumber))
                            showStatusText.postValue(true)
                        }
                        return@launch
                    }
                } else if (cardNumberRegister.value != null){
                    if (getUser.cardNumber != null) {
                        uiScope.launch {
                            registerFaceListener?.faceExited()
                            mStatusText.postValue(R.string.face_exits)
                        }
                        return@launch
                    }else if (getUser.email != null){
                        getUser.cardNumber = cardNumberRegister.value?.lowercase()
                        getUser.personName = nameEndUser.value
                        updateUserLocker(getUser)
                        uiScope.launch {
                            registerFaceListener?.faceExited()
                            statusText.postValue(context?.getString(R.string.face_exits_email, getUser.email))
                            showStatusText.postValue(true)
                        }
                        return@launch
                    }
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun updateUserLocker(userLockerModel: UserLockerModel) {
        ioScope.launch {
            mLoading.postValue(true)
            val updateUser = userLockerRepository.updateUser(userLockerModel)
            if (updateUser > 0) {
                mLoading.postValue(false)
            } else {
                mMessage.postValue(R.string.save_user_fail)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}