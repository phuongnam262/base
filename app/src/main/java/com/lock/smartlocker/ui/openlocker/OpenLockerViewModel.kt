package com.lock.smartlocker.ui.openlocker

import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class OpenLockerViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    var openLockerListener: OpenLockerListener? = null
    var numberLocker: Int? = null
    var typeOPen: String? = null
    lateinit var personCode: String

    private fun deletePerson() {
        ioScope.launch {
            /*try {
                val getResponse = personCode.let { userLockerRepository.deletePerson(it) }
                getResponse.errorCode.let {
                    if (it == Constant.ERROR_CODE_SUCCESS) {
                        val deleteUser = userLockerRepository.deleteUsedLocker(personCode)
                        if (deleteUser > 0) {
                            mSnackbar.postValue("Retrieve success")
                            openLockerListener?.openSuccess()
                        }
                        return@launch
                    } else {
                        mSnackbar.postValue(getResponse.message)
                    }
                }
            } catch (e: ApiException) {
                mSnackbar.postValue(e.message!!)
            } catch (e: NoInternetException) {
                mSnackbar.postValue(e.message!!)
            }*/
        }
    }

}