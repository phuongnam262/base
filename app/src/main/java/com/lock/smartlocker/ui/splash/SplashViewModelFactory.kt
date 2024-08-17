package com.lock.smartlocker.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.StartAppRepository

@Suppress("UNCHECKED_CAST")
class SplashViewModelFactory(
    private val repository: StartAppRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(repository) as T
    }
}