package com.lock.smartlocker.ui.category.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.databinding.ItemCategoryBinding
import com.lock.smartlocker.ui.category.CategoryViewModel
import com.xwray.groupie.databinding.BindableItem

class CategoryItem(
    private val category: Category,
    private val viewModel: CategoryViewModel,

    ) : BindableItem<ItemCategoryBinding>() {

    override fun getLayout() = R.layout.item_category

    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        viewBinding.category = category
        viewBinding.viewModel = viewModel

        val isSelected = viewModel.selectedCategory.value == category.categoryId
        viewBinding.isSelected = isSelected
    }
}