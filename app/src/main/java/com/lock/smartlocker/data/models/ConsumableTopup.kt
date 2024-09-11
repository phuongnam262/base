package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConsumableTopup(
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("set_point_sum") val setPointSum: String,
    @SerializedName("current_quantity") val currentQuantity: String,
    @SerializedName("consumable_image") val consumableImage: String,
    @SerializedName("consumable_name") val consumableName: String,
    @SerializedName("lockers") val lockers: ArrayList<LockerConsumable>
)