package com.lock.smartlocker.ui.facedetect

import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch
import kotlin.random.Random

class FaceViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    var faceListener: FaceListener? = null
    var availableLocker: Int? = null
    var typeOPen: String? = null
    fun detectImage(strBase64: String) {
        ioScope.launch {

            /*val imageBase64Request = ImageBase64Request(strBase64)
            val getDetectResponse = userLockerRepository.detectImage(imageBase64Request)
            getDetectResponse.errorCode.let {
                if (it == Constant.ERROR_CODE_SUCCESS) {
                    if (getDetectResponse.result.liveness == 1) {
                        searchPerson(strBase64)
                        return@launch
                    } else handelFaceFail(getDetectResponse)
                } else {
                    handelFaceFail(getDetectResponse)
                }
            }*/

        }
    }

    private fun handelFaceFail(detectImageResponse: DetectImageResponse) {
        /*mSnackbar.postValue(detectImageResponse.message)
        if (detectImageResponse.message.contains("not found")) {
            uiScope.launch {
                faceListener?.faceNotFound()
            }
        }*/
    }

    private fun searchPerson(strBase64: String) {
        ioScope.launch {

            /*val imageSearchRequest = ImageSearchRequest(strBase64)
            val getSearchResponse = userLockerRepository.searchPerson(imageSearchRequest)
            getSearchResponse.errorCode.let {
                if (it == Constant.ERROR_CODE_SUCCESS) {
                    if (getSearchResponse.result != null) {
                        when (typeOPen) {
                            Constant.TYPE_STORE -> {
                                if (getSearchResponse.result.similar < 0.7) {
                                    addPerson(strBase64)
                                } else {
                                    mSnackbar.postValue("You have already registered a locker.\nPlease back and chose Open or Retrieve locker.")
                                }
                            }

                            Constant.TYPE_OPEN, Constant.TYPE_RETRIEVE -> {
                                if (getSearchResponse.result.similar < 0.7) {
                                    mSnackbar.postValue("No record found, please using store and register your face")
                                } else {
                                    getSearchResponse.result.personCode?.let { it1 ->
                                        getUserLocker(
                                            it1
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        when (typeOPen) {
                            Constant.TYPE_STORE -> addPerson(strBase64)
                            Constant.TYPE_OPEN, Constant.TYPE_RETRIEVE -> mSnackbar.postValue("No record found, please using store and register your face")
                        }
                    }
                    return@launch
                } else {
                    mSnackbar.postValue(getSearchResponse.message)
                }
            }*/

        }
    }

    private fun addPerson(strBase64: String) {
        if (typeOPen == ConstantUtils.TYPE_ADMIN_CONSOLE) ioScope.launch {

            /* val personCodeGen = "x" + generateRandomNumber()
             val addPersonModel =
                 AddPersonRequest("1", personCodeGen, personCodeGen, strBase64, 1)
             val getResponse = userLockerRepository.addPerson(addPersonModel)
             getResponse.errorCode.let {
                 if (it == Constant.ERROR_CODE_SUCCESS) {
                     val userLockerModel =
                         UserLockerModel(0, personCodeGen, availableLocker, 1, 0)
                     val saveUser = userLockerRepository.saveUser(userLockerModel)
                     if (saveUser > 0) {
                         faceListener?.handleSuccess(availableLocker.toString(), personCodeGen)
                     } else {
                         mSnackbar.postValue("Assign locker fail")
                     }
                     return@launch
                 } else {
                     mSnackbar.postValue(getResponse.message)
                 }
             }*/

        }
    }

    private fun generateRandomNumber(): String {
        val randomNumber = Random.nextInt(100000, 999999)
        return randomNumber.toString()
    }

    private fun getUserLocker(personCode: String) {
        ioScope.launch {
            val getUser = userLockerRepository.getUsedLocker(personCode)
            if (getUser?.id != null) {
                //getUser.locker?.let { faceListener?.handleSuccess(it.toString(), personCode) }
            }
        }
    }

}