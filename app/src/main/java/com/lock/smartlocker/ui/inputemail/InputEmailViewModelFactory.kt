package com.lock.smartlocker.ui.inputemail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.StartAppRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository

class InputEmailViewModelFactory(
    private val repository: ManagerRepository,
    private val userLockerRepository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputEmailViewModel(repository, userLockerRepository) as T
    }
}