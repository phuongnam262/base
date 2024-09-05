package com.lock.smartlocker.util

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.roco.api.weigenRelay.weigenRelay

object KioskModeHelper {

    enum class Action(val value: String) {
        OPEN_GREEN_LIGHT("com.custom.green.light.open"),
        CLOSE_GREEN_LIGHT("com.custom.green.light.close"),
        OPEN_WHITE_LIGHT("com.custom.white.light.open"),
        CLOSE_WHITE_LIGHT("com.custom.white.light.close"),
        OPEN_RED_LIGHT("com.custom.red.light.open"),
        CLOSE_RED_LIGHT("com.custom.red.light.close"),
        OPEN_RELAY("com.custom.relay.open"),
        CLOSE_RELAY("com.custom.relay.close"),
        OPEN_STATUS_BAR("com.custom.open.statubar"),
        CLOSE_STATUS_BAR("com.custom.close.statubar"),
        SHOW_NAVIGATION_BAR("com.custom.show_navigationbar"),
        HIDE_NAVIGATION_BAR("com.custom.hide_navigationbar"),
        SHOW_STATUS_BAR("roco.systemui.show_statusbar"),
        HIDE_STATUS_BAR("roco.systemui.hide_statusbar")
    }

    fun sendCommand(context: Context, action: Action) {
        val intent = Intent(action.value).apply {
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        }
        context.sendBroadcast(intent)
    }

    fun handleGpioInput(context: Context, pin: Int, highMessage: String, lowMessage: String) {
        val ii = weigenRelay(context)
        ii.setGpioDirIn(pin)
        val result = ii.getGpioInData(pin)
        val message = if (result == 1) highMessage else lowMessage
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}