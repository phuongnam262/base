package com.lock.smartlocker.ui.retrieve

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.Categories
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.HardwareControllerRepository
import com.lock.smartlocker.data.repositories.ManagerRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class RetrieveViewModel(
    private val managerRepository: ManagerRepository,
    private val hardwareControllerRepository: HardwareControllerRepository
) : BaseViewModel() {

    val categories = MutableLiveData<List<Category>>()

    private val _categoriesRetrieve = MutableLiveData<List<Categories>>()
    val categoriesRetrieve: LiveData<List<Categories>> get() = _categoriesRetrieve

    var categoryIdSelected = MutableLiveData<String>()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        val jsonCategory = PreferenceHelper.getString(ConstantUtils.LIST_CATEGORY, "")
        val categoriesResponseType = object : TypeToken<GetListCategoryResponse>() {}.type
        val categoriesResponse: GetListCategoryResponse =
            Gson().fromJson(jsonCategory, categoriesResponseType)
        categories.postValue(categoriesResponse.categories)
    }

    fun getAllItemRetrieve() {
        ioScope.launch {
            mLoading.postValue(true)
            managerRepository.getAllItemRetrieve().apply {
                if (isSuccessful) {
                    if (data != null) {
                        val categoriesRetrieve = data.categories
                        categoriesRetrieve.map { category ->
                           val categoryMap = categories.value?.find { it.categoryId == category.categoryId}
                            categoryMap?.let {map->
                                category.categoryName = map.categoryName
                                category.modelRetrievies.map { model ->
                                    model.modelName = map.models.find { model.modelId == it.modelId }?.modelName ?: ""
                                }
                            }
                        }
                        _categoriesRetrieve.postValue(categoriesRetrieve)
                    }
                } else handleError(status)
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

//    fun onCategorySelected(category: Category) {
//        _categories.value?.map { it.isSelected = false }
//        categoryIdSelected.postValue(category.categoryId)
//        category.isSelected = true
//        val availableItem =
//            _availableItem.value?.find { it.categoryId == category.categoryId }
//        _availableModels.postValue(availableItem?.models ?: emptyList())
//    }
//
//    fun updateAvailableModels() {
//        if (categoryIdSelected.value != null){
//            val availableItem =
//                availableItem.value?.find { it.categoryId == categoryIdSelected.value }
//            _availableModels.postValue(
//                availableItem?.models ?: emptyList()
//            )
//        }
//    }
}