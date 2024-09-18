package com.lock.smartlocker.ui.returns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ReturnRepository

class ReturnViewModelFactory(
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReturnViewModel(returnRepository) as T
    }
}