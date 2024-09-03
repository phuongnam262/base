package com.lock.smartlocker.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository

@Suppress("UNCHECKED_CAST")
class SettingViewModelFactory(
    private val repository: ManagerRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(repository) as T
    }
}