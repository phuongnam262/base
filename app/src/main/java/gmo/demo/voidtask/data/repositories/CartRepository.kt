package gmo.demo.voidtask.data.repositories

import gmo.demo.voidtask.data.models.CartItem
import gmo.demo.voidtask.data.models.Product
import java.util.Date

class CartRepository {
    fun addToCart(product: Product, quantity: Int) {
        CartManager.addToCart(product, quantity)
    }

    fun getCartItems(): List<CartItem> = CartManager.getCartItems()

    fun removeFromCart(cartItemId: String) {
        CartManager.removeFromCart(cartItemId)
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        CartManager.updateQuantity(cartItemId, newQuantity)
    }

    fun getTotalPrice(): Double = CartManager.getTotalPrice()

    fun getTotalItems(): Int = CartManager.getTotalItems()

    fun clearCart() {
        CartManager.clearCart()
    }
}
