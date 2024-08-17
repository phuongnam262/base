package com.lock.smartlocker.ui.register_face

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

class RegisterFaceViewModelFactory(
    private val repository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterFaceViewModel(repository) as T
    }
}