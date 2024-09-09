package com.lock.smartlocker.ui.menu_register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ManagerRepository

class MenuRegisterViewModelFactory(
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MenuRegisterViewModel() as T
    }
}