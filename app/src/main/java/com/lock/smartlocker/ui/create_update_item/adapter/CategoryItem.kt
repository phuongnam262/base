package com.lock.smartlocker.ui.create_update_item.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.databinding.ItemCategoryBinding
import com.lock.smartlocker.ui.create_update_item.ItemViewModel
import com.xwray.groupie.databinding.BindableItem

class CategoryItem(
    private val category: Category,
    private val viewModel: ItemViewModel,

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