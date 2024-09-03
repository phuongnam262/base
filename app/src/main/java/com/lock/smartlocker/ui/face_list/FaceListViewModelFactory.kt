package com.lock.smartlocker.ui.face_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.UserFaceRepository

class FaceListViewModelFactory(
    private val faceRepository: UserFaceRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FaceListViewModel(faceRepository) as T
    }
}