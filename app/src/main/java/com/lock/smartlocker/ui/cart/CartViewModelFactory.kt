package com.lock.smartlocker.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.LoanRepository

class CartViewModelFactory(
    private val loanRepository: LoanRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(loanRepository) as T
    }
}