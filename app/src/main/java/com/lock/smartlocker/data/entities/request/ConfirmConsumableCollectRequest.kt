package com.lock.smartlocker.data.entities.request

import com.lock.smartlocker.data.models.ConfirmCollectItem

class ConfirmConsumableCollectRequest (
    var transaction_id: String? = null,
    var data_infos: List<ConfirmCollectItem>? = null
)