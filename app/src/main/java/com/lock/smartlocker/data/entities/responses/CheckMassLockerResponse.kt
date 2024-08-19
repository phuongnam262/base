package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.LockerStatus

class CheckMassLockerResponse(
    val locker_list: ArrayList<LockerStatus>,
)

