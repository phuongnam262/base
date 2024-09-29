package com.lock.smartlocker.ui.create_update_item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.data.repositories.ReturnRepository

class ItemViewModelFactory(
    private val returnRepository: ReturnRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemViewModel(returnRepository) as T
    }
}