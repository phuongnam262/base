package gmo.demo.voidtask.ui.myCart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gmo.demo.voidtask.data.models.CartItem
import gmo.demo.voidtask.data.repositories.CartRepository
import gmo.demo.voidtask.ui.base.BaseViewModel

class CartViewModel(private val repository: CartRepository) : BaseViewModel() {
    
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems
    
    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice
    
    private val _totalItems = MutableLiveData<Int>()
    val totalItems: LiveData<Int> = _totalItems
    
    init {
        loadCartItems()
    }
    
    fun loadCartItems() {
        val items = repository.getCartItems()
        _cartItems.value = items
        _totalPrice.value = repository.getTotalPrice()
        _totalItems.value = repository.getTotalItems()
    }
    
    fun removeFromCart(cartItemId: String) {
        repository.removeFromCart(cartItemId)
        loadCartItems()
    }
    
    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        if (newQuantity > 0) {
            repository.updateQuantity(cartItemId, newQuantity)
            loadCartItems()
        } else {
            removeFromCart(cartItemId)
        }
    }
    
    fun clearCart() {
        repository.clearCart()
        loadCartItems()
    }
}
