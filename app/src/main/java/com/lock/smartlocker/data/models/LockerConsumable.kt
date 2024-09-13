package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerConsumable(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("set_point") val setPoint: Int?,
    @SerializedName("current_quantity") var currentQuantity: Int?,
    @SerializedName("locker_size") var lockerSize: String?,
    @SerializedName("arrow_position") val arrowPosition: Int,
    var doorStatus: Int = 2,
)