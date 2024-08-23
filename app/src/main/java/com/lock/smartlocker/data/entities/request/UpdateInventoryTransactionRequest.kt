package com.lock.smartlocker.data.entities.request

import com.lock.smartlocker.data.models.InventoryItem

class UpdateInventoryTransactionRequest (
    var transaction_id: String? = null,
    var type_update: Int? = null,
    var serial_numbers: List<String>? = null,
    var serial_not_founds: List<String>? = null,
    var serial_skips: List<String>? = null,
)