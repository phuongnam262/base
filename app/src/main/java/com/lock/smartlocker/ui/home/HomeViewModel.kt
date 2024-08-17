package com.lock.smartlocker.ui.home

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.AddGroupModel
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
    var isServerOff = MutableLiveData<Boolean>()

    fun checkOpenServer() {
        ioScope.launch {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://localhost:8282/")
                .get()
                .build()

            try {
                val response = client.newCall(request).execute()
                response.isSuccessful
                isServerOff.postValue(true)
            } catch (e: IOException) {
                // Xử lý lỗi khi không có kết nối
                isServerOff.postValue(false)
            }
        }
    }

    fun addGroup() {
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