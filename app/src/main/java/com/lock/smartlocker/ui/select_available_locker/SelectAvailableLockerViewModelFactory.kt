package com.lock.smartlocker.ui.select_available_locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository

class SelectAvailableLockerViewModelFactory(
    private val hardwareControllerRepository: HardwareControllerRepository,
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectAvailableLockerViewModel(hardwareControllerRepository, returnRepository) as T
    }
}