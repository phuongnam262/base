package com.lock.smartlocker.ui.register_face

interface RegisterFaceListener {
    fun handleSuccess(personCode: String)
    fun faceNotFound()
}