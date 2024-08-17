package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Kiosk(
    @SerializedName("kiosk_config") val kioskConfig: KioskConfig
)
