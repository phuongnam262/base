package com.lock.smartlocker.ui.category_consumable.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.AvailableConsumable
import com.lock.smartlocker.databinding.ItemConsumableBinding
import com.lock.smartlocker.ui.category_consumable.CategoryConsumableViewModel
import com.xwray.groupie.databinding.BindableItem

class ConsumableItem(
    private val model: AvailableConsumable,
    private val viewModel: CategoryConsumableViewModel
) : BindableItem<ItemConsumableBinding>() {

    override fun getLayout() = R.layout.item_consumable

    override fun bind(viewBinding: ItemConsumableBinding, position: Int) {
        viewBinding.model = model

        viewBinding.btnSelect.setOnClickListener {
            viewModel.selectConsumable(model)
        }
    }
}