package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class RetrieviesModel(
    @SerializedName("model_id") val modelId:String,
    @SerializedName("lockers") val lockers: ArrayList<LockerRetrieve>,
    var modelName:String,
)
