package com.lock.smartlocker.ui.cart_consumable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.CreateTransactionRequest
import com.lock.smartlocker.data.models.CartConsumableItem
import com.lock.smartlocker.data.models.LockerInfoTransaction
import com.lock.smartlocker.data.models.TransactionItem
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch


class CartConsumableViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {
    val transactionId = MutableLiveData<String>()
    private val _listLockerInfo = MutableLiveData<MutableList<LockerInfoTransaction>>()
    val listLockerInfo: LiveData<MutableList<LockerInfoTransaction>> get() = _listLockerInfo
    val listCartItem = MutableLiveData<ArrayList<CartConsumableItem>>()

    fun increaseQuantity(cartItem: CartConsumableItem) {
        if (cartItem.quantity < cartItem.available && cartItem.quantity < cartItem.collectable) {
            cartItem.quantity += 1
            cartItem.collectable -= 1
            cartItem.available -= 1
            listCartItem.postValue(listCartItem.value)
        }else{
            mMessage.postValue(R.string.maximum_item_quantity_consumable)
        }
    }

    fun decreaseQuantity(cartItem: CartConsumableItem) {
        if (cartItem.quantity > 0) {
            if (cartItem.quantity == 1) {
                listCartItem.value?.remove(cartItem)
                cartItem.collectable += 1
                cartItem.available += 1
            } else {
                cartItem.quantity -= 1
                cartItem.collectable += 1
                cartItem.available += 1
            }
        }
        listCartItem.postValue(listCartItem.value)
    }

    fun createInventoryTransaction() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = CreateTransactionRequest()
            param.data_infos = listCartItem.value?.map { cartItem ->
                TransactionItem(
                    consumableId = cartItem.consumableId,
                    categoryId = cartItem.categoryId,
                    quantity = cartItem.quantity
                )
            }
            loanRepository.createConsumableTransaction(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        transactionId.postValue(data.transaction_id)
                        _listLockerInfo.postValue(data.locker_infos)
                        PreferenceHelper.writeString(ConstantUtils.LOCKER_INFOS, Gson().toJson(data))
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

}
