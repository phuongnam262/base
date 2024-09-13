package com.lock.smartlocker.ui.deposit_consumable

interface DepositConsumableListener {
    fun returnItemSuccess()
    fun topupItemSuccess()

    fun sendCommandOpenLockerSuccess(lockerId: String?)
}