package com.lock.smartlocker.ui.deposit_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModel

class DepositItemViewModelFactory(
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DepositItemViewModel(returnRepository) as T
    }
}