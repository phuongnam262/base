package com.lock.smartlocker.data.models

import java.io.Serializable

data class CartConsumableItem(
    val consumableId: String,
    var collectable: Int,
    var available: Int,
    val consumableName: String,
    val categoryId: String,
    var quantity: Int
): Serializable{
    var quantityString: String
        get() = quantity.toString()
        set(value) {
            quantity = value.toIntOrNull() ?: 0
        }
}
