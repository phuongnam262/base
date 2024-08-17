package com.lock.smartlocker.ui.admin_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AdminLoginViewModelFactory(

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminLoginViewModel() as T
    }
}