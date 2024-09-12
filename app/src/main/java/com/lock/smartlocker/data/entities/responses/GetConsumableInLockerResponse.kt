package com.lock.smartlocker.data.entities.responses

import com.google.gson.annotations.SerializedName
import com.lock.smartlocker.data.models.ConsumableInLocker

class GetConsumableInLockerResponse (
    @SerializedName("locker_name") val lockerName: String,
    @SerializedName("locker_id") val lockerId: String,
    @SerializedName("arrow_position") val arrowPosition: Int,
    @SerializedName("consumables") val consumables: ArrayList<ConsumableInLocker>,
)