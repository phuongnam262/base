package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class AvailableModel(
    @SerializedName("model_id") val modelId: String,
    @SerializedName("loanable") val loanable: String?,
    @SerializedName("available") var available: Int,
    @SerializedName("model_image") val modelImage: String,
    @SerializedName("model_name") val modelName: String
)