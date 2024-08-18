package com.lock.smartlocker.ui.select_faulty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SelectFaultyViewModelFactory(
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectFaultyViewModel() as T
    }
}