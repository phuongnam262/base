package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConsumableInLocker(
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("set_point") val setPoint: Int,
    @SerializedName("current_quantity") val currentQuantity: Int,
    @SerializedName("consumable_name") val consumableName: String,
    var inputQuantity: String?,
    var doorStatus: Int,
)