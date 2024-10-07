package com.lock.smartlocker.ui.media

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.VideoView
import com.lock.smartlocker.R
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.ui.base.BaseAppCompatActivity
import com.lock.smartlocker.util.ConstantUtils

class MediaActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        val videoView = findViewById<VideoView>(R.id.videoView)
        val textView = findViewById<TextView>(R.id.touchToPlay)
        val videoUrl = PreferenceHelper.getString(ConstantUtils.MEDIA_PATH, "")
        val uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)

        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
            videoView.start()
        }

        videoView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
                true
            } else {
                false
            }
        }

        textView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                finish()
                true
            } else {
                false
            }
        }
    }
}