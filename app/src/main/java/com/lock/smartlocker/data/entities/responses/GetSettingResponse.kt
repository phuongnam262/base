package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.AvailableLocker
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.data.models.Terminal

class GetSettingResponse(
    val terminal: Terminal,
    val available_locker: ArrayList<AvailableLocker>,
    val lockers: ArrayList<Locker>,
)

