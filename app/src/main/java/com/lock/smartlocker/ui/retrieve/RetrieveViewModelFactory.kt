package com.lock.smartlocker.ui.retrieve

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository

class RetrieveViewModelFactory(
    private val repository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RetrieveViewModel(repository, hardwareControllerRepository) as T
    }
}