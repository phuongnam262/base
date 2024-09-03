package com.lock.smartlocker.ui.manage_locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository

@Suppress("UNCHECKED_CAST")
class ManageLockerViewModelFactory(
    private val repository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageLockerViewModel(repository, hardwareControllerRepository) as T
    }
}