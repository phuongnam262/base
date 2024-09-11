package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class ConsumableCategories(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") var categoryName: String,
    @SerializedName("consumables") val consumables: ArrayList<ConsumableTopup>,
    var isSelected: Boolean,
)
