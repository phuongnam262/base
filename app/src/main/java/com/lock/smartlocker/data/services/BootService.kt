package com.lock.smartlocker.data.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.lock.smartlocker.R
import com.lock.smartlocker.ui.home.HomeActivity
import com.lock.smartlocker.ui.splash.SplashActivity

class BootService : Service() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "BootService onStartCommand")
        startForeground(NOTIFICATION_ID, createNotification())

        Handler(Looper.getMainLooper()).postDelayed({
            Log.d(TAG, "Starting app from BootService")
            startApp()
            stopSelf()
        }, DELAY_MILLIS)

        return START_STICKY
    }

    private fun startApp() {
        val startAppIntent = Intent(this, SplashActivity::class.java)
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(startAppIntent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Boot Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        // Tạo PendingIntent để mở ứng dụng khi nhấp vào thông báo
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, SplashActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("App Started")
            .setContentText("Your app has been started after device boot")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val TAG = "BootService"
        private const val CHANNEL_ID = "BootServiceChannel"
        private const val NOTIFICATION_ID = 52
        private const val DELAY_MILLIS = 10000L // 8 seconds delay
    }
}