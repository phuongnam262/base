package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class InventoryItem(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("model_id") val modelId: String,
    @SerializedName("quantity") val quantity: Int
)
