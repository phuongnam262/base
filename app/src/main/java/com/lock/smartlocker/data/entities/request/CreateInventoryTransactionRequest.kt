package com.lock.smartlocker.data.entities.request

import com.lock.smartlocker.data.models.InventoryItem

class CreateInventoryTransactionRequest (
    var transaction_type: Int? = null,
    var data_infos: List<InventoryItem>? = null
)