package com.lock.smartlocker.data.entities.responses

data class BaseFaceResponse (
    val errorCode: String,
    val message: String,
    val result: Int,
    val status: Int
)