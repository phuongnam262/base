package com.lock.smartlocker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.data.repositories.UserFaceRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(
    private val userFaceRepository: UserFaceRepository,
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userFaceRepository, returnRepository) as T
    }
}