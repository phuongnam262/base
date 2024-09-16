package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class AvailableConsumable(
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("collectable") var collectable: Int,
    @SerializedName("available") var available: Int,
    @SerializedName("consumable_image") val consumableImage: String,
    @SerializedName("consumable_name") val consumableName: String,
)