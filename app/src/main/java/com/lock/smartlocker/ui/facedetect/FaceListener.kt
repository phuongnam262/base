package com.lock.smartlocker.ui.facedetect

interface FaceListener {
    fun handleSuccess(locker: String, personCode: String)
    fun faceNotFound()
}