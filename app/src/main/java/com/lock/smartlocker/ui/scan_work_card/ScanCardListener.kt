package com.lock.smartlocker.ui.scan_work_card

interface ScanCardListener {
    fun handleSuccess(name: String, cardNumber: String)
}