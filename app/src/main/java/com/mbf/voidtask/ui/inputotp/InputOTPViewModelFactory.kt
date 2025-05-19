package com.mbf.voidtask.ui.inputotp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mbf.voidtask.data.repositories.AppRepository

class InputOTPViewModelFactory(
    private val repository: AppRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputOTPViewModel(repository) as T
    }
}