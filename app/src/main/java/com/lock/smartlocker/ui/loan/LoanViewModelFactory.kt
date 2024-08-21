package com.lock.smartlocker.ui.loan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoanViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoanViewModel() as T
    }
}