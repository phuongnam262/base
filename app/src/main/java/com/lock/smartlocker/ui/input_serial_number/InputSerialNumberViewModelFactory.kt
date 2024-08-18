package com.lock.smartlocker.ui.input_serial_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ReturnRepository

class InputSerialNumberViewModelFactory(
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InputSerialNumberViewModel(returnRepository) as T
    }
}