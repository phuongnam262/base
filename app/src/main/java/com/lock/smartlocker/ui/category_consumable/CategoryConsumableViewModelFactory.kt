package com.lock.smartlocker.ui.category_consumable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.data.repositories.ReturnRepository

class CategoryConsumableViewModelFactory(
    private val managerRepository: ManagerRepository
    ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryConsumableViewModel(managerRepository) as T
    }
}