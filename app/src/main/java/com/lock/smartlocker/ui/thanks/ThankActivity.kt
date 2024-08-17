package com.lock.smartlocker.ui.thanks

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lock.smartlocker.R
import com.lock.smartlocker.ui.home.HomeActivity
import java.util.Timer
import kotlin.concurrent.timerTask

class ThankActivity : AppCompatActivity() {

    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank)
        val btnEnd = findViewById<Button>(R.id.btnEnd);
        btnEnd.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            timer.cancel()
        }
        goSleepThread()
    }

    private fun goSleepThread() {
        timer.schedule(timerTask {
            val intent = Intent(this@ThankActivity, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }, 3000)

    }
}