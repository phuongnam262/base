package com.lock.smartlocker.ui.category_consumable_collect.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ConsumableAvailableItem
import com.lock.smartlocker.databinding.ItemCategoryBinding
import com.lock.smartlocker.ui.category_consumable_collect.CategoryConsumableCollectViewModel
import com.xwray.groupie.databinding.BindableItem

class CategoryItem(
    private val category: ConsumableAvailableItem,
    private val viewModel: CategoryConsumableCollectViewModel,

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