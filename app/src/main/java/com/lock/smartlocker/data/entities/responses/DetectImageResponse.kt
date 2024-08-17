package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.entities.FaceInfoModel

data class DetectImageResponse(
    val errorCode: String,
    val message: String,
    val result: FaceInfoModel,
    val status: Int
)
