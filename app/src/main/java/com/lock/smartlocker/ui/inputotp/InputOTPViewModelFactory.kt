package com.lock.smartlocker.ui.inputotp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.StartAppRepository

class InputOTPViewModelFactory(
    private val repository: ManagerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputOTPViewModel(repository) as T
    }
}