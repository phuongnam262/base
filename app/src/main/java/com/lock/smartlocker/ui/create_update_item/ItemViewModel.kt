package com.lock.smartlocker.ui.create_update_item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.request.ItemRequest
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.data.models.Model
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.launch

class ItemViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var itemListener: ItemListener? = null
    var itemReturn = MutableLiveData<ItemReturn>()
    var serialNumber = MutableLiveData<String>()
    var itemType: Int = 0
    var typeCreateItem = MutableLiveData<Int>(0) // 0: new, 1: created, 2: updated
    var showInfoItem = MutableLiveData<Boolean>()
    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories
    private val _models = MutableLiveData<List<Model>>()
    val models: LiveData<List<Model>> get() = _models
    var categorySelected = MutableLiveData<Category>()
    var modelSelected = MutableLiveData<String>()
    var isUpdateFlow = MutableLiveData<Boolean>(false)

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

    fun onCategorySelected(category: Category) {
        _categories.value?.map { it.isSelected = false }
        categorySelected.postValue(category)
        category.isSelected = true
        _models.postValue(category.models)
        itemReturn.value?.categoryId = category.categoryId
        itemReturn.value?.categoryName = category.categoryName
    }

    fun selectModel(model: Model){
        modelSelected.postValue(model.modelId)
        itemReturn.value?.modelId = model.modelId
        itemReturn.value?.modelName = model.modelName
        itemReturn.value?.modelImage = model.image
    }

    fun createItem() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = ItemRequest()
            param.serial_number = serialNumber.value
            param.model_id = modelSelected.value
            param.type = itemType
            returnRepository.createItem(param).apply {
                if (isSuccessful) {
                    showStatusText.postValue(false)
                    uiScope.launch {
                        itemListener?.handleSuccess()
                    }
                } else {
                    if (status == ConstantUtils.EMAIL_NOT_CORRECT_FORMAT) status = ConstantUtils.ERROR_SERIAL_EXISTED
                    handleError(status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }

    fun updateItem() {
        ioScope.launch {
            mLoading.postValue(true)
            val param = ItemRequest()
            param.serial_number = serialNumber.value
            param.model_id = modelSelected.value
            param.type = itemType
            returnRepository.updateItem(param).apply {
                if (isSuccessful) {
                    showStatusText.postValue(false)
                    uiScope.launch {
                        itemListener?.handleSuccess()
                    }
                } else {
                    handleError(status)
                }
            }
        }.invokeOnCompletion { mLoading.postValue(false) }
    }
}

