package com.lock.smartlocker.data.entities.request

import com.google.gson.annotations.SerializedName

class CheckCardRequest(
    @SerializedName("card_number") var cardNumber: String? = null
)
