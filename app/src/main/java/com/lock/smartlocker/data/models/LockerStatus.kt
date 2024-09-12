package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerStatus(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("door_status") var doorStatus: Int,
    @SerializedName("item_status") val itemStatus: Int,
    @SerializedName("arrow_position") val arrowPosition: Int,
)