package com.lock.smartlocker.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.AvailableCategory
import com.lock.smartlocker.data.models.AvailableModel
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.LoanRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val loanRepository: LoanRepository,
) : BaseViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _availableCategories = MutableLiveData<List<AvailableCategory>>()
    val availableCategories: LiveData<List<AvailableCategory>> get() = _availableCategories

    private val _availableModels = MutableLiveData<List<AvailableModel>>()
    val availableModels: LiveData<List<AvailableModel>> get() = _availableModels

    private val _selectedCategory = MutableLiveData<String>("")
    val selectedCategory: LiveData<String> get() = _selectedCategory

    private val _cartItems = MutableLiveData<MutableList<CartItem>>()
    val cartItems: LiveData<MutableList<CartItem>> get() = _cartItems


    init {
        loadCategories()
        loadAvailableItem()
    }

     fun loadCategories() {
        val jsonCategory = PreferenceHelper.getString(ConstantUtils.LIST_CATEGORY, "")
        val categoriesResponseType = object : TypeToken<GetListCategoryResponse>() {}.type
        val categoriesResponse: GetListCategoryResponse = Gson().fromJson(jsonCategory, categoriesResponseType)

        _categories.postValue(categoriesResponse.categories)
    }

    private fun loadAvailableItem() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = GetAvailableItemRequest()
            param.transaction_type = 1
            loanRepository.getAvailableItem(param).apply {
                if (isSuccessful) {
                    if (data != null ) {
                        _availableCategories.postValue(data.categories)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    private fun loadAvailableModels(categoryId: String) {
        val availableCategory = availableCategories.value?.find { it.categoryId == categoryId }
        _availableModels.postValue(availableCategory?.models ?: emptyList())
    }

    fun onCategorySelected(categoryId: String) {
        _selectedCategory.value = categoryId
        loadAvailableModels(categoryId)
    }

    fun addToCart(model: AvailableModel) {
        val category = availableCategories.value?.find {
            it.models.any { availableModel -> availableModel.modelId == model.modelId }
        } ?: return

        if (model.available == 0 || (model.loanable != null && model.loanable == 0)) {
            return
        }

        val updatedModel = model.copy(
            available = if (model.available > 0) model.available - 1 else model.available,
            loanable = model.loanable?.let {
                if (it > 0) it - 1 else it
            }
        )

        _availableModels.value = _availableModels.value?.map {
            if (it.modelId == model.modelId) updatedModel else it
        }

        val updatedCategories = availableCategories.value?.map { availableCategory ->
            if (availableCategory.categoryId == category.categoryId) {
                val updatedModels = availableCategory.models.map {
                    if (it.modelId == model.modelId) updatedModel else it
                }
                availableCategory.copy(models = updatedModels)
            } else {
                availableCategory
            }
        }
        _availableCategories.postValue(updatedCategories!!)

        // Thêm vào giỏ hàng
        val currentCartItems = _cartItems.value?.toMutableList() ?: mutableListOf()

        val existingCartItem = currentCartItems.find { it.model.modelId == model.modelId }

        val categoryLimit = category.loanable ?: Int.MAX_VALUE
        val totalQuantityInCategory = currentCartItems
            .filter { it.category.categoryId == category.categoryId }
            .sumOf { it.quantity }

        if (existingCartItem != null) {
            // Kiểm tra nếu tổng số lượng trong giỏ hàng chưa vượt quá giới hạn của category
            if (existingCartItem.quantity < (categoryLimit - totalQuantityInCategory)) {
                existingCartItem.quantity += 1
            }
        } else {
            if (totalQuantityInCategory < categoryLimit) {
                val cartItem = CartItem(
                    model = updatedModel,
                    category = category,
                    quantity = 1
                )
                currentCartItems.add(cartItem)
            }
        }

        _cartItems.postValue(currentCartItems)
    }
}