package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class AvailableItem(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("loanable") val loanable: Int?,
    @SerializedName("models") val models: List<AvailableModel>
)