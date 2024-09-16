package com.lock.smartlocker.data.entities.request

import com.lock.smartlocker.data.models.TransactionItem

class CreateTransactionRequest (
    var data_infos: List<TransactionItem>? = null
)