package com.lock.smartlocker.ui.category_consumable_collect.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.AvailableConsumable
import com.lock.smartlocker.databinding.ItemConsumableCollectBinding
import com.lock.smartlocker.ui.category_consumable_collect.CategoryConsumableCollectViewModel
import com.xwray.groupie.databinding.BindableItem

class ConsumableItem(
    private val model: AvailableConsumable,
    private val viewModel: CategoryConsumableCollectViewModel
) : BindableItem<ItemConsumableCollectBinding>() {

    override fun getLayout() = R.layout.item_consumable_collect

    override fun bind(viewBinding: ItemConsumableCollectBinding, position: Int) {
        viewBinding.model = model

        viewBinding.btnAddToCart.setOnClickListener {
            viewModel.selectConsumable(model)
        }
    }
}