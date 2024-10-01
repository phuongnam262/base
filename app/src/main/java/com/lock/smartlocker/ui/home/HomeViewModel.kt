package com.lock.smartlocker.ui.home

import LocaleHelper
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.AddGroupModel
import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.NoInternetException
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class HomeViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    var homeListener: HomeListener? = null
    var isOpenLocalServer = MutableLiveData<Boolean>()
    var atinInnitSuccess = MutableLiveData<Boolean>()

    private val _language = MutableLiveData<String>()
    val language: LiveData<String> = _language

    fun setNewLocale(context: Context, languageCode: String) {
        LocaleHelper.setLocale(context, languageCode)
        _language.value = languageCode
    }

    fun checkATINOpenServer(){
        ioScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://localhost:8282/status")
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                response.isSuccessful
                isOpenLocalServer.postValue(true)
                checkATINReady()
            } catch (e: IOException) {
                // Xử lý lỗi khi không có kết nối
                isOpenLocalServer.postValue(false)
            }
        }
    }

    private fun checkATINReady() {
        ioScope.launch {
            try {
                val getResponse = userLockerRepository.getStatusAPI()
                getResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS){
                        if (getResponse.result == 0 && getResponse.status == 0) {
                            atinInnitSuccess.postValue(true)
                            return@launch
                        }else atinInnitSuccess.postValue(false)
                    }else atinInnitSuccess.postValue(false)
                }
            } catch (e: ApiException) {
                atinInnitSuccess.postValue(false)
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                atinInnitSuccess.postValue(false)
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_network)
            }
        }
    }

    fun getGroup() {
        ioScope.launch {
            try {
                val getResponse = userLockerRepository.getGroup("G1")
                getResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS) {
                        if (getResponse.message.contains("Group not found")) addGroup()
                        else return@launch
                    }else addGroup()
                }
            } catch (e: ApiException) {
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_network)
            }
        }
    }


    private fun addGroup() {
        ioScope.launch {
            try {
                val addGroupModel = AddGroupModel("G1", "Group 1", 1)
                val getResponse = userLockerRepository.addGroup(addGroupModel)
                getResponse.errorCode.let {
                    if (it == ConstantUtils.ERROR_CODE_SUCCESS)
                        return@launch
                }
            } catch (e: ApiException) {
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_message)
            } catch (e: NoInternetException) {
                mLoading.postValue(false)
                mMessage.postValue(R.string.error_network)
            }
        }
    }

}