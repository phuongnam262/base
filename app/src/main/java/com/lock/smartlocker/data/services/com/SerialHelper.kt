package com.lock.smartlocker.data.services.com

import android.util.Log
import android_serialport_api.SerialPort
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidParameterException
abstract class SerialHelper @JvmOverloads constructor(
    sPort: String = "/dev/s3c2410_serial0",
    iBaudRate: Int = 9600
) {
    private var mSerialPort: SerialPort? = null
    private var mOutputStream: OutputStream? = null
    private var mInputStream: InputStream? = null
    private var mReadThread: ReadThread? = null
    private var mSendThread: SendThread? = null
    private var sPort = "/dev/s3c2410_serial0"

    //----------------------------------------------------
    var baudRate: Int = 9600
        private set

    //----------------------------------------------------
    var isOpen: Boolean = false
        private set
    private val _bLoopData = byteArrayOf(0x30)
    private val iDelay = 50

    //----------------------------------------------------
    init {
        this.sPort = sPort
        this.baudRate = iBaudRate
    }

    constructor(sPort: String, sBaudRate: String) : this(sPort, sBaudRate.toInt())

    //----------------------------------------------------
    @Throws(SecurityException::class, IOException::class, InvalidParameterException::class)
    fun open() {
        mSerialPort = SerialPort(File(sPort), baudRate, 0)
        mOutputStream = mSerialPort!!.outputStream
        mInputStream = mSerialPort!!.inputStream
        mReadThread = ReadThread()
        mReadThread!!.start()
        mSendThread = SendThread()
        mSendThread!!.setSuspendFlag()
        mSendThread!!.start()
        isOpen = true
    }

    //----------------------------------------------------
    fun close() {
        if (mReadThread != null) mReadThread!!.interrupt()
        if (mSerialPort != null) {
            mSerialPort!!.close()
            mSerialPort = null
        }
        isOpen = false
    }

    //----------------------------------------------------
    fun send(bOutArray: ByteArray?) {
        try {
//			Log.e("test","send bOutArray.length"+bOutArray.length);
            mOutputStream!!.write(bOutArray)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //----------------------------------------------------
    private inner class ReadThread : Thread() {
        override fun run() {
            super.run()
            while (!isInterrupted) {
                try {
                    if (mInputStream == null) return
                    //byte[] buffer=new byte[16];
                    val buffer = ByteArray(512)
                    val size = mInputStream!!.read(buffer)
                    Log.e("test", "size = $size")

                    if (size > 0) {
                        val ComRecData = ComBean(sPort, buffer, size)
                        onDataReceived(ComRecData)
                    }
                    try {
                        sleep(400) //延时50ms
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    return
                }
            }
        }
    }

    //----------------------------------------------------
    private inner class SendThread : Thread() {
        var suspendFlag: Boolean = true // 控制线程的执行
        override fun run() {
            super.run()
            while (!isInterrupted) {
                synchronized(this) {
                    while (suspendFlag) {
                        try {
                            (this as Object).wait()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
                send(getbLoopData())
                try {
                    sleep(iDelay.toLong())
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        //线程暂停
        fun setSuspendFlag() {
            this.suspendFlag = true
        }

        //唤醒线程
        @Synchronized
        fun setResume() {
            this.suspendFlag = false
            (this as Object).notify()
        }
    }

    fun setBaudRate(iBaud: Int): Boolean {
        if (isOpen) {
            return false
        } else {
            baudRate = iBaud
            return true
        }
    }

    fun setBaudRate(sBaud: String): Boolean {
        val iBaud = sBaud.toInt()
        return setBaudRate(iBaud)
    }

    //----------------------------------------------------
    fun getbLoopData(): ByteArray {
        return _bLoopData
    }

    protected abstract fun onDataReceived(ComRecData: ComBean?)
}