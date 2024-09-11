package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConsumableAvailableItem(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("consumables") val consumables: List<AvailableConsumable>,
    var isSelected: Boolean,
)