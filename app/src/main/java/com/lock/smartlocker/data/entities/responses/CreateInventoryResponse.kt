package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.LockerInfo

class CreateInventoryResponse(
    val transaction_id: String,
    val locker_infos: ArrayList<LockerInfo>
)

