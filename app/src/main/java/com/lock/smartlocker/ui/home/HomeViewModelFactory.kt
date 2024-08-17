package com.lock.smartlocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val repository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}