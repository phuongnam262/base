package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class AvailableLocker(
    @SerializedName("size") val size: String,
    @SerializedName("total_available_locker") val totalAvailableLocker: Int
)
