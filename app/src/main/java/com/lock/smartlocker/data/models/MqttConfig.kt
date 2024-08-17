package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class MqttConfig(
    @SerializedName("host") val host: String,
    @SerializedName("port") val port: Int,
    @SerializedName("client_id") val clientId: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("project_code") val projectCode: String
)