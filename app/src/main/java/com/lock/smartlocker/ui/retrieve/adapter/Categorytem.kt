package com.lock.smartlocker.ui.retrieve.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Categories
import com.lock.smartlocker.databinding.ItemCategoryBinding
import com.lock.smartlocker.ui.retrieve.RetrieveViewModel
import com.xwray.groupie.databinding.BindableItem

class Categorytem(
    private val category: Categories,
    private val viewModel: RetrieveViewModel,

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