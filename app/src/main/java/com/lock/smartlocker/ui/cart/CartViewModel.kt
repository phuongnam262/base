package com.lock.smartlocker.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.entities.request.CreateInventoryTransactionRequest
import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.data.models.InventoryItem
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch


class CartViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(emptyList())
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    fun setCartItems(items: List<CartItem>) {
        _cartItems.value = items
    }


    fun increaseQuantity(cartItem: CartItem) {
        val currentItems = _cartItems.value ?: return

        val categoryLimit = cartItem.category.loanable ?: Int.MAX_VALUE

        val totalQuantityInCategory = currentItems
            .filter { it.category.categoryId == cartItem.category.categoryId }
            .sumOf { it.quantity }

        val availableModel = cartItem.model
        if (availableModel.available > 0 &&
            (availableModel.loanable == null || availableModel.loanable > 0) &&
            totalQuantityInCategory < categoryLimit) {

            val updatedItems = currentItems.map { item ->
                if (item.model.modelId == cartItem.model.modelId) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    item
                }
            }
            _cartItems.value = updatedItems
        }
    }


    fun decreaseQuantity(cartItem: CartItem) {
        val currentItems = _cartItems.value ?: return

        val updatedItems = currentItems.mapNotNull { item ->
            if (item.model.modelId == cartItem.model.modelId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null
                }
            } else {
                item
            }
        }
        _cartItems.value = updatedItems
    }

    fun createInventoryTransaction(openType: Int) {
        ioScope.launch {
            mLoading.postValue(true)
            val param = CreateInventoryTransactionRequest()
            param.transaction_type = openType
            param.data_infos = _cartItems.value?.map { cartItem ->
                InventoryItem(
                    modelId = cartItem.model.modelId,
                    categoryId = cartItem.category.categoryId,
                    quantity = cartItem.quantity
                )
            }
            loanRepository.getAvailableItem(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        _availableCategories.postValue(data.categories)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

}
