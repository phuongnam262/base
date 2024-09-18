package com.lock.smartlocker.data.entities.responses

data class BaseFaceResponse<T> (
    val errorCode: String,
    val message: String,
    val result: T,
    val status: Int
)