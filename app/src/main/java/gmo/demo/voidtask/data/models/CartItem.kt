package gmo.demo.voidtask.data.models

import java.util.Date

data class CartItem(
    val id: String = System.currentTimeMillis().toString(),
    val productId: String,
    val productName: String,
    val price: String, // Changed from Double to String
    val quantity: Int,
    val image: String,
    val orderTime: Date = Date()
) {
    val totalPrice: Double
        get() = price.toDoubleOrNull()?.let { it * quantity } ?: 0.0
}
