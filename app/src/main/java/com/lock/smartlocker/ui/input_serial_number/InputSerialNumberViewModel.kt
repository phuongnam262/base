package com.lock.smartlocker.ui.input_serial_number

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class InputSerialNumberViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var scanSerialNumberListener: InputSerialNumberListener? = null
    val serialNumber = MutableLiveData<String>()
    var isReturnFlow = false
    val itemReturnData = MutableLiveData<ItemReturn?>()
    val isItemDetailVisible = MutableLiveData(false)
    val isUpdateItem = MutableLiveData(false)
    val isCreateItem = MutableLiveData(false)
    val typeInput = MutableLiveData<String?>()

    fun getItemReturn() {
        ioScope.launch {
            if (serialNumber.value.isNullOrEmpty()) {
                isItemDetailVisible.postValue(false)
                mStatusText.postValue(R.string.error_input_serial_empty)
                return@launch
            }
            mLoading.postValue(true)
            val param = GetItemReturnRequest()
            param.serial_number = serialNumber.value
            returnRepository.getItemReturn(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        data.reasonFaulty = ""
                        showStatusText.postValue(false)
                        itemReturnData.postValue(data)
                        isUpdateItem.postValue(false)
                        isItemDetailVisible.postValue(true)
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
                isItemDetailVisible.postValue(false)
                isUpdateItem.postValue(false)
                isCreateItem.postValue(false)
                mStatusText.postValue(R.string.error_input_serial_empty)
                return@launch
            }else showStatusText.postValue(false)
            mLoading.postValue(true)
            val param = GetItemReturnRequest()
            param.serial_number = serialNumber.value
            returnRepository.getItemTopup(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        data.reasonFaulty = ""
                        showStatusText.postValue(false)
                        itemReturnData.postValue(data)
                        isItemDetailVisible.postValue(true)
                        isUpdateItem.postValue(true)
                        isCreateItem.postValue(false)
                        scanSerialNumberListener?.onGetItemSuccess()
                    } else {
                        isItemDetailVisible.postValue(false)
                    }
                } else {
                    if (status == ConstantUtils.SERIAL_NUMBER_INVALID_1) {
                        val itemReturn = ItemReturn(
                            transactionId = "",
                            serialNumber = serialNumber.value ?: "",
                            modelName = "",
                            modelId = "",
                            categoryName = "",
                            categoryId = "",
                            loaneeEmail = "",
                            modelImage = "null",
                            type = 0,
                            lockerId = "",
                            lockerName = "",
                            arrowPosition = 1,
                            doorStatus = 2,
                            reasonFaulty = ""
                        )
                        itemReturnData.postValue(itemReturn)
                        isUpdateItem.postValue(false)
                        isCreateItem.postValue(true)
                        isItemDetailVisible.postValue(true)
                    }
                    else {
                        handleError(status)
                        isUpdateItem.postValue(false)
                        isItemDetailVisible.postValue(false)
                    }
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}



