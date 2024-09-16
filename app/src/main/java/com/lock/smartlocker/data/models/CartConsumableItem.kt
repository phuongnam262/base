package com.lock.smartlocker.data.models

data class CartConsumableItem(
    val consumableId: String,
    var collectable: Int,
    var available: Int,
    val consumableName: String,
    val categoryId: String,
    var quantity: Int
){
    var quantityString: String
        get() = quantity.toString()
        set(value) {
            quantity = value.toIntOrNull() ?: 0
        }
}
