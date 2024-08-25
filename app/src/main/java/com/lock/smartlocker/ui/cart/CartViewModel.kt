package com.lock.smartlocker.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.CreateInventoryTransactionRequest
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.data.models.InventoryItem
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch


class CartViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {
    val transactionId = MutableLiveData<String>()
    private val _listLockerInfo = MutableLiveData<MutableList<LockerInfo>>()
    val listLockerInfo: LiveData<MutableList<LockerInfo>> get() = _listLockerInfo
    val listCartItem = MutableLiveData<ArrayList<CartItem>>()

    fun increaseQuantity(cartItem: CartItem) {
        if (cartItem.quantity < cartItem.available && cartItem.quantity < cartItem.loanable) {
            cartItem.quantity += 1
            listCartItem.postValue(listCartItem.value)
        }else{
            mMessage.postValue(R.string.maximun_item_quantity)
        }
    }


    fun decreaseQuantity(cartItem: CartItem) {
        if (cartItem.quantity == 1){
            listCartItem.value?.remove(cartItem)
        }else{
            cartItem.quantity -= 1
        }
        listCartItem.postValue(listCartItem.value)
    }

    fun createInventoryTransaction(openType: Int) {
        ioScope.launch {
            mLoading.postValue(true)
            val param = CreateInventoryTransactionRequest()
            param.transaction_type = openType
            param.data_infos = listCartItem.value?.map { cartItem ->
                InventoryItem(
                    modelId = cartItem.modelId,
                    categoryId = cartItem.categoryId,
                    quantity = cartItem.quantity
                )
            }
            loanRepository.createInventoryTransaction(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        transactionId.postValue(data.transaction_id)
                        data.locker_infos.map {
                            it.quantity = listCartItem.value?.find { cartItem ->
                                cartItem.modelId == it.modelId
                            }?.quantity ?: 0
                        }
                        _listLockerInfo.postValue(data.locker_infos)
                        PreferenceHelper.writeString(ConstantUtils.LOCKER_INFOS, Gson().toJson(data))
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

}
