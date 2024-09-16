package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConsumableCollect(
    @SerializedName("consumable_name") val consumableName: String,
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("current_quantity") val currentQuantity: Int,
    @SerializedName("set_point") val setPoint: Int,
    @SerializedName("take_number") val takeNumber: Int,
)