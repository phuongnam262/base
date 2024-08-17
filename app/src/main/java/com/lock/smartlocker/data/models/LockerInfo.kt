package com.lock.smartlocker.data.models

data class LockerInfo(
    val lockerId: String,
    val lockerName: String,
    val serialNumber: String,
    val modelName: String,
    val modelId: String,
    val categoryName: String
)