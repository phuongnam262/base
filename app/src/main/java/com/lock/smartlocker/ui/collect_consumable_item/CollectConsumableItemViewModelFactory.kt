package com.lock.smartlocker.ui.collect_consumable_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.LoanRepository

class CollectConsumableItemViewModelFactory(
    private val loanRepository: LoanRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CollectConsumableItemViewModel(loanRepository, hardwareControllerRepository) as T
    }
}