package com.lock.smartlocker.ui.category_consumable.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ConsumableCategories
import com.lock.smartlocker.databinding.ItemCategoryBinding
import com.lock.smartlocker.ui.category_consumable.CategoryConsumableViewModel
import com.xwray.groupie.databinding.BindableItem

class CategoryItem(
    private val category: ConsumableCategories,
    private val viewModel: CategoryConsumableViewModel,

    ) : BindableItem<ItemCategoryBinding>() {

    override fun getLayout() = R.layout.item_category

    override fun bind(viewBinding: ItemCategoryBinding, position: Int) {
        viewBinding.categoryName = category.categoryName
        viewBinding.isSelected = category.isSelected
        viewBinding.llCategory.setOnClickListener {
            viewModel.onCategorySelected(category)
        }
    }
}