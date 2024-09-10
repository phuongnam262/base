package com.lock.smartlocker.ui.inputemail

interface InputEmailListener {
    fun consumerLoginSuccess(email: String?)
    fun consumerLoginFail(email: String?, status: String?)
}