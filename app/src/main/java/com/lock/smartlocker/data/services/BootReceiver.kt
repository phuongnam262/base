package com.lock.smartlocker.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == Intent.ACTION_POWER_CONNECTED ||
            intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED) {
            Log.d(TAG, "Received action: ${intent.action}")
            startApp(context)
        }
    }

    private fun startApp(context: Context) {
        val startAppIntent = Intent(context, BootService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startAppIntent)
        } else {
            context.startService(startAppIntent)
        }
    }

    companion object {
        private const val TAG = "BootReceiver"
    }
}