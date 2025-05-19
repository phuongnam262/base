package com.lock.basesource.data.models

import com.google.gson.annotations.SerializedName

data class EndUser(
    @SerializedName("full_name") val fullName: String,
    @SerializedName("card_number") val cardNumber: String,
    @SerializedName("user_token") val userToken: String
)
