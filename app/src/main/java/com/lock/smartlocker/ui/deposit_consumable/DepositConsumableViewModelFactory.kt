package com.lock.smartlocker.ui.deposit_consumable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository

class DepositConsumableViewModelFactory(
    private val managerRepository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DepositConsumableViewModel(managerRepository, hardwareControllerRepository) as T
    }
}