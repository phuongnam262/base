package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Categories(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") var categoryName: String,
    @SerializedName("model_retrievies") val modelRetrievies: ArrayList<RetrieviesModel>,
    var isSelected: Boolean,
)
