package com.lock.smartlocker.data.entities.request

import java.io.Serializable

data class ReturnItemRequest(
    var serial_number: String? = null,
    var locker_id: String? = null,
    var is_faulty: Boolean? = false,
    var reason_faulty: String? = null
) : Serializable