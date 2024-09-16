package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerInfoCollect(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("arrow_position") val arrowPosition: Int,
    var doorStatus: Int,

    @SerializedName("consumable_name") val consumableName: String,
    @SerializedName("consumable_id") val consumableId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("current_quantity") val currentQuantity: Int,
    @SerializedName("set_point") val setPoint: Int,
    @SerializedName("take_number") var takeNumber: Int,
) {
    var takeNumberString: String
        get() = takeNumber.toString()
        set(value) {
            takeNumber = value.toIntOrNull() ?: 0
        }
}