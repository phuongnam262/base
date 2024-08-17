package com.lock.smartlocker.ui.openlocker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

@Suppress("UNCHECKED_CAST")
class OpenLockerViewModelFactory(
    private val repository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OpenLockerViewModel(repository) as T
    }
}