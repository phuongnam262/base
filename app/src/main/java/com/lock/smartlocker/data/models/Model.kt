package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Model(
    @SerializedName("model_id") val modelId: String,
    @SerializedName("model_name") val modelName: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("image") val image: String
)
