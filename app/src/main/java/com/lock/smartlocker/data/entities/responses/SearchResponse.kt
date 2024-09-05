package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.PersonModel

data class SearchResponse(
    val errorCode: String,
    val message: String,
    val result: PersonModel?,
    val status: Int
)
