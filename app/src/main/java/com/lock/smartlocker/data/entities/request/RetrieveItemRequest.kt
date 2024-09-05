package com.lock.smartlocker.data.entities.request

import com.google.gson.annotations.SerializedName

data class RetrieveItemRequest(
    @SerializedName("serial_numbers") var serialNumbers: List<String>? = null
)