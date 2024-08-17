package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.Setting
import com.lock.smartlocker.data.models.Terminal

class TerminalLoginResponse(
    val api_token: String,
    val terminal: Terminal,
    val setting: Setting
)

