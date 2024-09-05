package com.lock.smartlocker.data.models

data class FaceInfoModel(
    val gender: Int = 0,
    var feature: String? = null,
    var liveness: Int = 0,
    var age: Int = 0
)
