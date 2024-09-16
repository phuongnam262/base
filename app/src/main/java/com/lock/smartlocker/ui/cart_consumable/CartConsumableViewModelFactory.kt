package com.lock.smartlocker.ui.cart_consumable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository

class CartConsumableViewModelFactory(
    private val loanRepository: LoanRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartConsumableViewModel(loanRepository) as T
    }
}