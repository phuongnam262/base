package com.lock.smartlocker.util

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import java.util.Timer
import java.util.TimerTask

object CountdownTimer {
    val timeoutString = MutableLiveData<String>()
    private var timer: Timer? = null
    private var remainingTime: Long = 0
    var onTimeout: (() -> Unit)? = null

    fun startCountdownTimer(minute: Int) {
        stopTimer()
        remainingTime = minute * 60L
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            @SuppressLint("DefaultLocale")
            override fun run() {
                if (remainingTime > 0) {
                    remainingTime--
                    val minutes = remainingTime / 60
                    val seconds = remainingTime % 60
                    val formattedTime = String.format("%02d:%02d", minutes, seconds)
                    timeoutString.postValue(formattedTime)
                } else {
                    stopTimer()
                    onTimeout?.invoke()
                }
            }
        }, 0, 1000)
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}