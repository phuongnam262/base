package gmo.demo.voidtask.data.repositories

import gmo.demo.voidtask.data.models.CartItem
import gmo.demo.voidtask.data.models.Product
import java.util.Date

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addToCart(product: Product, quantity: Int) {
        val existingItem = cartItems.find { it.productId == product.id.toString() }
        
        if (existingItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(
                quantity = existingItem.quantity + quantity,
                orderTime = Date()
            )
        } else {
            // Thêm sản phẩm mới vào giỏ hàng
            val cartItem = CartItem(
                productId = product.id ?: "",
                productName = product.title ?: "",
                price = product.price ?: "",
                quantity = quantity,
                image = product.image ?: ""
            )
            cartItems.add(cartItem)
        }
    }

    fun getCartItems(): List<CartItem> = cartItems.toList()

    fun removeFromCart(cartItemId: String) {
        cartItems.removeAll { it.id == cartItemId }
    }

    fun updateQuantity(cartItemId: String, newQuantity: Int) {
        val index = cartItems.indexOfFirst { it.id == cartItemId }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = newQuantity)
        }
    }

    fun getTotalPrice(): Double = cartItems.sumOf { it.totalPrice }

    fun getTotalItems(): Int = cartItems.sumOf { it.quantity }

    fun clearCart() {
        cartItems.clear()
    }
}
