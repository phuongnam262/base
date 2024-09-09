package com.lock.smartlocker.data.entities.responses

import com.google.gson.annotations.SerializedName
import com.lock.smartlocker.data.models.EndUser

class CheckCardResponse(
   @SerializedName("end_user") val endUser: EndUser
)

