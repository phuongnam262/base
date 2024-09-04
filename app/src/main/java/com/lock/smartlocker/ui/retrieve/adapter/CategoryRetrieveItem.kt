package com.lock.smartlocker.ui.retrieve.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Categories
import com.lock.smartlocker.databinding.ItemCategoryRetrieveBinding
import com.lock.smartlocker.ui.retrieve.RetrieveViewModel
import com.xwray.groupie.databinding.BindableItem

class CategoryRetrieveItem(
    private val category: Categories,
    private val viewModel: RetrieveViewModel,

    ) : BindableItem<ItemCategoryRetrieveBinding>() {

    override fun getLayout() = R.layout.item_category_retrieve

    override fun bind(viewBinding: ItemCategoryRetrieveBinding, position: Int) {
        viewBinding.category = category
        viewBinding.llCategory.setOnClickListener {
            viewModel.onCategorySelected(category)
        }
    }
}