package com.lock.smartlocker.ui.scan_work_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.StartAppRepository

class ScanWorkCardViewModelFactory(
    private val managerRepository: ManagerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanWorkCardViewModel(managerRepository) as T
    }
}