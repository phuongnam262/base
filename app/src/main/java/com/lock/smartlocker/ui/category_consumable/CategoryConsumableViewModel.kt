package com.lock.smartlocker.ui.category_consumable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.models.AvailableConsumable
import com.lock.smartlocker.data.models.ConsumableAvailableItem
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CategoryConsumableViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {
    private val _categoriesConsumable = MutableLiveData<List<ConsumableAvailableItem>>()
    val categoriesConsumable: LiveData<List<ConsumableAvailableItem>> get() = _categoriesConsumable

    private val _listConsumable = MutableLiveData<List<AvailableConsumable>>()
    val listConsumable: LiveData<List<AvailableConsumable>> get() = _listConsumable
    var categoryIdSelected = MutableLiveData<String>()

    fun getConsumableAvailableItem() {
        ioScope.launch {
            mLoading.postValue(true)
            loanRepository.getConsumableAvailableItem().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val listId = ArrayList<String>()
                        _categoriesConsumable.postValue(data.categories)
                    }
                } else {
                    handleError(status)
                    _categoriesConsumable.postValue(emptyList())
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onCategorySelected(category: ConsumableAvailableItem) {
        _categoriesConsumable.value?.map { it.isSelected = false }
        categoryIdSelected.postValue(category.categoryId)
        category.isSelected = true
        val categoriesItem =
            _categoriesConsumable.value?.find { it.categoryId == category.categoryId }
        categoriesItem.let { categories ->
            _listConsumable.postValue(categories?.consumables ?: emptyList())
        }
    }

    fun selectConsumable(model: AvailableConsumable) {

    }
}