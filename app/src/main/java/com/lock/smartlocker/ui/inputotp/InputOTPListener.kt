package com.lock.smartlocker.ui.inputotp

interface InputOTPListener {
    fun verifySuccess(email: String?)

    fun verifyFail()
}