package com.lock.smartlocker.ui.deposit_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository

class DepositItemViewModelFactory(
    private val returnRepository: ReturnRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DepositItemViewModel(returnRepository, hardwareControllerRepository) as T
    }
}