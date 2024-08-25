package com.lock.smartlocker.ui.input_serial_number

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class InputSerialNumberViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var scanSerialNumberListener: InputSerialNumberListener? = null
    val serialNumber = MutableLiveData<String>()

    val modelImage = MutableLiveData<String?>("")
    val itemReturnData = MutableLiveData<ItemReturn?>()
    val isItemDetailVisible = MutableLiveData(false)
    val typeInput = MutableLiveData<String?>()

    fun getItemReturn() {
        ioScope.launch {
            if (serialNumber.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_input_serial_empty)
                return@launch
            }
            mLoading.postValue(true)
            val param = GetItemReturnRequest()
            param.serial_number = serialNumber.value
            returnRepository.getItemReturn(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        showStatusText.postValue(false)
                        itemReturnData.postValue(data)
                        isItemDetailVisible.postValue(true)
                        modelImage.postValue(getModelImageUrl(data.modelId))
                    } else {
                        isItemDetailVisible.postValue(false)
                    }
                } else {
                    handleError(status)
                    isItemDetailVisible.postValue(false)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun getItemTopup() {
        ioScope.launch {
            if (serialNumber.value.isNullOrEmpty()) {
                mStatusText.postValue(R.string.error_input_serial_empty)
                return@launch
            }
            mLoading.postValue(true)
            val param = GetItemReturnRequest()
            param.serial_number = serialNumber.value
            returnRepository.getItemTopup(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        showStatusText.postValue(false)
                        itemReturnData.postValue(data)
                        isItemDetailVisible.postValue(true)
                        modelImage.postValue(getModelImageUrl(data.modelId))
                    } else {
                        isItemDetailVisible.postValue(false)
                    }
                } else {
                    handleError(status)
                    isItemDetailVisible.postValue(false)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun getModelImageUrl(modelId: String) : String? {
        val jsonCategory = PreferenceHelper.getString(ConstantUtils.LIST_CATEGORY, "")
        val categoriesResponseType = object : TypeToken<GetListCategoryResponse>() {}.type
        val categoriesResponse: GetListCategoryResponse = Gson().fromJson(jsonCategory, categoriesResponseType)

        val model = categoriesResponse.categories
            .flatMap { category -> category.models }
            .firstOrNull { model -> model.modelId == modelId }

        return model?.image
    }
}



