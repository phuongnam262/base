package com.lock.smartlocker.data.entities.request

import com.google.gson.annotations.SerializedName

class ReportConsumableRequest (
    @SerializedName("locker_id") var lockerId: String? = null,
    @SerializedName("consumable_id") var consumableId: String? = null,
    @SerializedName("reason") var reason: String? = null
)