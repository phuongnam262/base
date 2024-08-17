package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class SystemSetting(
    @SerializedName("systemName") val systemName: String,
    @SerializedName("lastSaved") val lastSaved: Any?,
    @SerializedName("enable2FA") val enable2FA: Boolean
)
