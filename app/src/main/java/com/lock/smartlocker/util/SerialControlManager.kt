package com.lock.smartlocker.util

import android.content.Context
import android.widget.Toast
import com.lock.smartlocker.data.services.com.ComBean
import com.lock.smartlocker.data.services.com.SerialHelper
import java.io.IOException
import java.security.InvalidParameterException

object SerialControlManager {
    private var comA: SerialControl? = null

    fun initialize(context: Context) {
        comA = SerialControl("/dev/ttysWK2", 9600)
        openComPort(context)
    }

    fun getSerialControl(): SerialControl? = comA

    private fun openComPort(context: Context) {
        try {
            comA?.open()
        } catch (e: SecurityException) {
            Toast.makeText(context, "Permission denied for serial port.", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(context, "Failed to open serial port.", Toast.LENGTH_SHORT).show()
        } catch (e: InvalidParameterException) {
            Toast.makeText(context, "Invalid parameters.", Toast.LENGTH_SHORT).show()
        }
    }

    fun closeComPort() {
        comA?.close()
    }

    class SerialControl(sPort: String?, iBaudRate: Int) : SerialHelper(sPort!!, iBaudRate) {
        var onDataReceivedListener: ((String) -> Unit)? = null

        override fun onDataReceived(comRecData: ComBean?) {
            val receivedData = comRecData?.bRec?.decodeToString()?.trim()
            receivedData?.let { onDataReceivedListener?.invoke(it) }
        }
    }
}