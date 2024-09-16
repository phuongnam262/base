package com.lock.smartlocker.ui.scan_work_card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository

class ScanWorkCardViewModelFactory(
    private val managerRepository: ManagerRepository,
    private val userLockerRepository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanWorkCardViewModel(managerRepository, userLockerRepository) as T
    }
}