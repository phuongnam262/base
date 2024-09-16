package com.lock.smartlocker.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.AvailableItem
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

    private val _availableItem = MutableLiveData<List<AvailableItem>>()
    val availableItem: LiveData<List<AvailableItem>> get() = _availableItem

    private val _availableModels = MutableLiveData<List<AvailableModel>>()
    val availableModels: LiveData<List<AvailableModel>> get() = _availableModels

    var categoryIdSelected = MutableLiveData<String>()
    private val updatedListCart: ArrayList<CartItem> = ArrayList()
    val listCartItem = MutableLiveData<ArrayList<CartItem>>()
    init {
        loadCategories()
    }

    private fun loadCategories() {
        val jsonCategory = PreferenceHelper.getString(ConstantUtils.LIST_CATEGORY, "")
        val categoriesResponseType = object : TypeToken<GetListCategoryResponse>() {}.type
        val categoriesResponse: GetListCategoryResponse =
            Gson().fromJson(jsonCategory, categoriesResponseType)
        _categories.postValue(categoriesResponse.categories)
    }

    fun loadAvailableItem(openType: Int) {
        ioScope.launch {
            mLoading.postValue(true)
            val param = GetAvailableItemRequest()
            param.transaction_type = openType
            loanRepository.getAvailableItem(param).apply {
                if (isSuccessful) {
                    if (data != null) {
                            listCartItem.value?.let { listCart ->
                                val listModel = data.categories.flatMap { it.models }
                                listModel.map { item ->
                                    listCart.map { cart ->
                                        if (item.modelId == cart.modelId) {
                                            item.available -= cart.quantity
                                        }
                                    }

                                }
                            }

                        _availableItem.postValue(data.categories)

                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun onCategorySelected(category: Category) {
        _categories.value?.map { it.isSelected = false }
        categoryIdSelected.postValue(category.categoryId)
        category.isSelected = true
        val availableItem =
            _availableItem.value?.find { it.categoryId == category.categoryId }
        _availableModels.postValue(availableItem?.models ?: emptyList())
    }

    fun updateAvailableModels() {
        if (categoryIdSelected.value != null){
            val availableItem =
                availableItem.value?.find { it.categoryId == categoryIdSelected.value }
            _availableModels.postValue(
                availableItem?.models ?: emptyList()
            )
        }
    }

    fun addToCart(model: AvailableModel) {
        val cartItem = listCartItem.value?.find { it.modelId == model.modelId }
        if (cartItem != null) {
            if(model.loanable != null){
                if (model.available > 0 && cartItem.quantity < model.loanable.toInt()) {
                    cartItem.quantity += 1
                    model.available -= 1
                    listCartItem.postValue(listCartItem.value)
                }
            }else{
                if (model.available > 0) {
                    cartItem.quantity += 1
                    model.available -= 1
                    listCartItem.postValue(listCartItem.value)
                }
            }
        } else {
            val cart = categoryIdSelected.value?.let {
                if(model.loanable != null) {
                    model.loanable.toInt().let { it1 ->
                        CartItem(
                            modelId = model.modelId,
                            modelName = model.modelName,
                            categoryId = it,
                            loanable = it1,
                            available = model.available,
                            quantity = 1
                        )
                    }
                }else{
                    CartItem(
                        modelId = model.modelId,
                        modelName = model.modelName,
                        categoryId = it,
                        loanable = 9999,
                        available = model.available,
                        quantity = 1
                    )
                }
            }
            if (cart != null) {
                updatedListCart.add(cart)
                listCartItem.postValue(updatedListCart)
            }
            if (model.available > 0) {
                model.available -= 1
            }
        }
    }
}