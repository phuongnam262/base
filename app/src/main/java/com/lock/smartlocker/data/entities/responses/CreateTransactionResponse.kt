package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.LockerInfoTransaction

class CreateTransactionResponse(
    val transaction_id: String,
    val locker_infos: ArrayList<LockerInfoTransaction>
)

