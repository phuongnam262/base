package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("limit") val limit: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("image") val image: String,
    @SerializedName("models") val models: List<Model>,
    @SerializedName("reason_faulties") val reasonFaulties: List<String>
)
