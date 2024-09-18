package com.lock.smartlocker.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository

class CategoryViewModelFactory(
    private val loanRepository: LoanRepository,
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(loanRepository) as T
    }
}