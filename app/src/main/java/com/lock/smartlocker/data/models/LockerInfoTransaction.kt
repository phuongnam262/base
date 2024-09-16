package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerInfoTransaction(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("arrow_position") val arrowPosition: Int,
    @SerializedName("consumable_collects") val consumableCollects: ArrayList<ConsumableCollect>,
    var doorStatus: Int,
)