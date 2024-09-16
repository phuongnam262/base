package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConfirmCollectItem(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("quantity") val quantity: Int
)
