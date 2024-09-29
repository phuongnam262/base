package com.lock.smartlocker.data.entities.request

data class ItemRequest(
    var serial_number: String? = null,
    var model_id: String? = null,
    var type: Int = 0
)