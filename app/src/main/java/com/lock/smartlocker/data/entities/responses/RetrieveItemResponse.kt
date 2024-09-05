package com.lock.smartlocker.data.entities.responses

import com.google.gson.annotations.SerializedName

class RetrieveItemResponse(
    @SerializedName("locker_retrieves") val lockerRetrieves: ArrayList<String>,
)

