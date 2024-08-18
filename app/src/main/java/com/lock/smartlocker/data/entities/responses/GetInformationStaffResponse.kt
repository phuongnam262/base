package com.lock.smartlocker.data.entities.responses

import com.google.gson.annotations.SerializedName

class GetInformationStaffResponse(
    @SerializedName("locker_available") val lockerAvailable: ArrayList<String>,
    @SerializedName("item_faulty") val itemFaulty: Int,
    @SerializedName("expired_reservation") val expiredReservation: Int
    )

