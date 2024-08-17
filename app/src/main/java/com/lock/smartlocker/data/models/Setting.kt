package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Setting(
    @SerializedName("kiosk") val kiosk: Kiosk,
    @SerializedName("mqtt") val mqtt: MqttConfig,
    @SerializedName("languages") val languages: List<Any>,
    @SerializedName("work_flow") val workflow: Workflow,
    @SerializedName("system_setting") val systemSetting: SystemSetting
)
