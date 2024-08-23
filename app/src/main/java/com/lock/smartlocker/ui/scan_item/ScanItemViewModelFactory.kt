package com.lock.smartlocker.ui.scan_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository

class ScanItemViewModelFactory(
    private val loanRepository: LoanRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScanItemViewModel(loanRepository) as T
    }
}