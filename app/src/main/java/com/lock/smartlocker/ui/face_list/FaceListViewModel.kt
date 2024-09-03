package com.lock.smartlocker.ui.face_list

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.UserLockerModel
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch

class FaceListViewModel(
    private val faceRepository: UserFaceRepository
) : BaseViewModel() {

    var faceListener: FaceListListener? = null
    private val _faces = MutableLiveData<List<UserLockerModel>>()
    val faces: MutableLiveData<List<UserLockerModel>> get() = _faces

    fun loadListFaces() {
        ioScope.launch {
            mLoading.postValue(true)
            val getUsers = faceRepository.getAllUserLocker()
            if (getUsers.isNotEmpty()) {
                _faces.postValue(getUsers)
            }else{
                mStatusText.postValue(R.string.empty_face_list)
                uiScope.launch {faceListener?.dataEmpty()}
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun removeFace(userLockerModel: UserLockerModel) {
        ioScope.launch {
            try {
                userLockerModel.personCode.let {personCode ->
                    val getResponse = faceRepository.deletePerson(personCode!!)
                    getResponse.errorCode.let {
                        if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                            val deleteUser = faceRepository.deleteUserLocker(personCode)
                            if (deleteUser > 0) {
                                _faces.value?.let { currentList ->
                                    val updatedList = currentList.toMutableList()
                                    updatedList.remove(userLockerModel)
                                    _faces.postValue(updatedList)
                                }
                            }
                            return@launch
                        } else {
                            statusText.postValue(getResponse.message)
                            showStatusText.postValue(true)
                        }
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }
    }

    fun removeAllFace() {
        ioScope.launch {
            try {
                val getResponse = faceRepository.deleteAllPerson("1")
                getResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        val deleteUser = faceRepository.deleteAllUserLocker()
                        if (deleteUser > 0) {
                            _faces.postValue(emptyList())
                        }
                        return@launch
                    } else {
                        statusText.postValue(getResponse.message)
                        showStatusText.postValue(true)
                    }
                }
            } catch (e: ApiException) {
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mMessage.postValue(R.string.error_network)
            }
        }
    }
}

