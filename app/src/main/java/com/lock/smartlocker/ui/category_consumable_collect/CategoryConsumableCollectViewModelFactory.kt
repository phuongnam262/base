package com.lock.smartlocker.ui.category_consumable_collect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.data.repositories.ManagerRepository

class CategoryConsumableCollectViewModelFactory(
    private val loanRepository: LoanRepository
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryConsumableCollectViewModel(loanRepository) as T
    }
}