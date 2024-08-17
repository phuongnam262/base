package com.lock.smartlocker.ui.facedetect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

@Suppress("UNCHECKED_CAST")
class FaceViewModelFactory(
    private val repository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FaceViewModel(repository) as T
    }
}