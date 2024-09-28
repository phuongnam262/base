package com.lock.smartlocker.ui.recognize_face

interface RecognizeFaceListener {
    fun handleSuccess(personName: String?)

    fun faceExited()
    fun faceNotFound()
    fun consumerLoginSuccess(email: String?)
    fun consumerLoginFail(email: String?, status: String?)
}