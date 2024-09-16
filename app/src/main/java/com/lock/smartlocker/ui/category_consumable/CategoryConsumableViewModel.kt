package com.lock.smartlocker.ui.category_consumable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.models.ConsumableCategories
import com.lock.smartlocker.data.models.ConsumableTopup
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CategoryConsumableViewModel(
    private val managerRepository: ManagerRepository
) : BaseViewModel() {
    private val _categoriesConsumable = MutableLiveData<List<ConsumableCategories>>()
    val categoriesConsumable: LiveData<List<ConsumableCategories>> get() = _categoriesConsumable

    private val _listConsumable = MutableLiveData<List<ConsumableTopup>>()
    val listConsumable: LiveData<List<ConsumableTopup>> get() = _listConsumable
    var categoryIdSelected = MutableLiveData<String>()

    var lockers = MutableLiveData<List<LockerConsumable>>()

    init {
        getConsumableAvailableItem()
    }

    private fun getConsumableAvailableItem() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getConsumable().apply {
                if (isSuccessful) {
                    if (data != null) {
                        _categoriesConsumable.postValue(data.categories)
                    }
                } else {
                    handleError(status)
                    _categoriesConsumable.postValue(emptyList())
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onCategorySelected(category: ConsumableCategories) {
        _categoriesConsumable.value?.map { it.isSelected = false }
        categoryIdSelected.postValue(category.categoryId)
        category.isSelected = true
        val categoriesItem =
            _categoriesConsumable.value?.find { it.categoryId == category.categoryId }
        categoriesItem.let { categories ->
            _listConsumable.postValue(categories?.consumables ?: emptyList())
        }
    }

    fun selectConsumable(model: ConsumableTopup) {
        lockers.postValue(model.lockers)
    }
}