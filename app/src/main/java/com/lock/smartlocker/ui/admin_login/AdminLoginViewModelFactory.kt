package com.lock.smartlocker.ui.admin_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository

class AdminLoginViewModelFactory(
    private val repository: ManagerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminLoginViewModel(repository) as T
    }
}