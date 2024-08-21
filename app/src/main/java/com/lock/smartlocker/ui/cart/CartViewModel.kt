package com.lock.smartlocker.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.ui.base.BaseViewModel


class CartViewModel : BaseViewModel() {
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

        val updatedItems = currentItems.map { item ->
            if (item.model.modelId == cartItem.model.modelId) {
                if (item.quantity > 1) {
                    item.copy(quantity = item.quantity - 1)
                } else {
                    null
                }
            } else {
                item
            }
        }.filterNotNull()
        _cartItems.value = updatedItems
    }

}
