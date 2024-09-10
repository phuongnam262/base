package com.lock.smartlocker.ui.category_consumable.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.AvailableModel
import com.lock.smartlocker.databinding.ItemModelBinding
import com.lock.smartlocker.ui.category.CategoryViewModel
import com.xwray.groupie.databinding.BindableItem

class ConsumableItem(
    private val model: AvailableModel,
    private val viewModel: CategoryViewModel
) : BindableItem<ItemModelBinding>() {

    override fun getLayout() = R.layout.item_model

    override fun bind(viewBinding: ItemModelBinding, position: Int) {
        viewBinding.model = model

        viewBinding.btnAddToCart.setOnClickListener {
            viewModel.addToCart(model)
        }
    }
}