package com.lock.smartlocker.ui.category_consumable_collect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lock.smartlocker.data.models.AvailableConsumable
import com.lock.smartlocker.data.models.CartConsumableItem
import com.lock.smartlocker.data.models.ConsumableAvailableItem
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class CategoryConsumableCollectViewModel(
    private val loanRepository: LoanRepository
) : BaseViewModel() {
    private val _consumableAvailableItems = MutableLiveData<List<ConsumableAvailableItem>>()
    val consumableAvailableItems: LiveData<List<ConsumableAvailableItem>> get() = _consumableAvailableItems

    private val _availableConsumables = MutableLiveData<List<AvailableConsumable>>()
    val availableConsumables: LiveData<List<AvailableConsumable>> get() = _availableConsumables

    var categoryIdSelected = MutableLiveData<String>()
    private val updatedListCart: ArrayList<CartConsumableItem> = ArrayList()
    val listCartItem = MutableLiveData<ArrayList<CartConsumableItem>>()

    init {
        getConsumableAvailableItem()
    }

    fun getConsumableAvailableItem() {
        ioScope.launch {
            mLoading.postValue(true)
            loanRepository.getConsumableAvailableItem().apply {
                if (isSuccessful) {
                    if (data != null) {
                        listCartItem.value?.let { listCart ->
                            val listModel = data.categories.flatMap { it.consumables }
                            listModel.map { item ->
                                listCart.map { cart ->
                                    if (item.consumableId == cart.consumableId) {
                                        item.available -= cart.quantity
                                        item.collectable -= cart.quantity
                                    }
                                }

                            }
                        }
                        _consumableAvailableItems.postValue(data.categories)

                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onCategorySelected(category: ConsumableAvailableItem) {
        _consumableAvailableItems.value?.map { it.isSelected = false }
        categoryIdSelected.postValue(category.categoryId)
        category.isSelected = true
        val categoriesItem =
            _consumableAvailableItems.value?.find { it.categoryId == category.categoryId }
        categoriesItem.let { categories ->
            _availableConsumables.postValue(categories?.consumables ?: emptyList())
        }
    }

    fun updateAvailableConsumable() {
        if (categoryIdSelected.value != null){
            val availableItem =
                consumableAvailableItems.value?.find { it.categoryId == categoryIdSelected.value }
            _availableConsumables.postValue(
                availableItem?.consumables ?: emptyList()
            )
        }
    }

    fun selectConsumable(consumable: AvailableConsumable) {
        val cartConsumableItem = listCartItem.value?.find { it.consumableId == consumable.consumableId }
        if (cartConsumableItem != null) {
            if (consumable.available > 0 && consumable.collectable > 0) {
                cartConsumableItem.quantity += 1
                consumable.available -= 1
                consumable.collectable -= 1
                cartConsumableItem.collectable -= 1
                listCartItem.postValue(listCartItem.value)
            }
        } else {
            if (consumable.available > 0) {
                val cart = categoryIdSelected.value?.let {
                    CartConsumableItem(
                        consumableId = consumable.consumableId,
                        consumableName = consumable.consumableName,
                        categoryId = it,
                        collectable = consumable.collectable - 1,
                        available = consumable.available - 1,
                        quantity = 1
                    )
                }
                if (consumable.available > 0) {
                    consumable.available -= 1
                }
                if (consumable.collectable > 0) {
                    consumable.collectable -= 1
                }
                if (cart != null) {
                    updatedListCart.add(cart)
                    listCartItem.postValue(updatedListCart)
                }
            }
        }
    }
}