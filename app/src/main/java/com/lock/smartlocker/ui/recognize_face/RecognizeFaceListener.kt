package com.lock.smartlocker.ui.recognize_face

interface RecognizeFaceListener {
    fun handleSuccess(personCode: String, email: String)

    fun faceExited()
    fun faceNotFound()
    fun consumerLoginSuccess(email: String?)
}