package com.lock.smartlocker.ui.thank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThankViewModelFactory(
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThankViewModel() as T
    }
}