package com.lock.smartlocker.util

import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

object AppTimer {
    val timerString = MutableLiveData<String>()
    val timerHour = MutableLiveData<String>()
    private var timer: Timer? = null

    fun startTimer() {
        if (timer == null) {
            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    val currentTime = Date()
                    val formattedTime = SimpleDateFormat(
                        "HH:mm dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(currentTime)
                    val formattedHour = SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(currentTime)
                    timerString.postValue(formattedTime)
                    timerHour.postValue(formattedHour)
                }
            }, 0, 1000)
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }
}