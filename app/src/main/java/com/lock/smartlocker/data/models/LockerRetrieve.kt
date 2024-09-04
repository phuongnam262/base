package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class LockerRetrieve(
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("serial_number") val serialNumber: String,
    @SerializedName("model_name") var modelName: String,
    var doorStatus: Int,
    var retrieveStatus : Int,
)