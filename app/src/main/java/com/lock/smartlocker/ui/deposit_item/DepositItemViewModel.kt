package com.lock.smartlocker.ui.deposit_item

import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class DepositItemViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var depositItemListener: DepositItemListener? = null

    // Biến lưu trữ trạng thái cửa
    val doorStatus = MutableLiveData<Int?>()

    fun checkStatus() {
        ioScope.launch {
            // ... (gọi API để kiểm tra trạng thái)

            // Giả sử API trả về một Map<String, Any> chứa dữ liệu
            val responseData = mapOf(
                "locker_id" to "280321jijojxojwq",
                "door_status" to 1, // 1 là đóng, 0 là mở
                "item_status" to 1
            )

            // Cập nhật trạng thái cửa
            val doorStatusValue = responseData["door_status"] as? Int
            doorStatus.postValue(doorStatusValue)
        }
    }

    fun returnItem(returnItemRequest: ReturnItemRequest) {
        ioScope.launch {
            returnRepository.returnItem(returnItemRequest).apply {
                if (isSuccessful) {
                    if (data != null) {
                        depositItemListener?.returnItemSuccess()
                    }
                }else handleError(status)
            }
        }
    }
}