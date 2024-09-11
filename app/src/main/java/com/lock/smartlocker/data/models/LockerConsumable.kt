package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerConsumable(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("set_point") val setPoint: Int,
    @SerializedName("current_quantity") var currentQuantity: Int,
    @SerializedName("position") var position: Int,
    var doorStatus: Int = 2,
)