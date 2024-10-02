package com.lock.smartlocker.ui.select_faulty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.ui.base.BaseViewModel
import com.lock.smartlocker.util.ConstantUtils

class SelectFaultyViewModel(

) : BaseViewModel() {
    var selectFaultyListener: SelectFaultyListener? = null

    private val _spinnerItems = MutableLiveData<List<String>>()
    val spinnerItems: LiveData<List<String>> = _spinnerItems

    private val _selectedFaultyReason = MutableLiveData<String>()
    val selectedFaultyReason: LiveData<String> = _selectedFaultyReason

    fun loadSpinnerItems(categoryId: String) {
        val jsonCategory = PreferenceHelper.getString(ConstantUtils.LIST_CATEGORY, "")
        if (jsonCategory.isNotEmpty()) {
            try {
                val listType = object : TypeToken<ArrayList<Category>>() {}.type
                val categoryList = Gson().fromJson<ArrayList<Category>>(jsonCategory, listType)

                // Lọc danh mục theo categoryId
                val category = categoryList.find { it.categoryId == categoryId }
                val reasonFaulties = category?.reasonFaulties?.distinct() ?: listOf("No faulties available")

                _spinnerItems.value = reasonFaulties
            } catch (e: Exception) {
                _spinnerItems.value = listOf("Error parsing data")
                e.printStackTrace()
            }
        } else {
            _spinnerItems.value = listOf("No data available")
        }
    }

    fun setSelectedFaultyReason(reason: String) {
        _selectedFaultyReason.value = reason
    }
}

