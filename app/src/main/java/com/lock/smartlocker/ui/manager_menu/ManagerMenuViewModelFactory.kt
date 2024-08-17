package com.lock.smartlocker.ui.manager_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

@Suppress("UNCHECKED_CAST")
class ManagerMenuViewModelFactory(
    private val repository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManagerMenuViewModel(repository) as T
    }
}