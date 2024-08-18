package com.lock.smartlocker.ui.admin_dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository

class AdminDashboardViewModelFactory(
    private val repository: ManagerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminDashboardViewModel(repository) as T
    }
}