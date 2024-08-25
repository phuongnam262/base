package com.lock.smartlocker.data.models

data class CartItem(
    val modelId: String,
    val modelName: String,
    val categoryId: String,
    val loanable: Int,
    val available: Int,
    var quantity: Int
)
