package com.lock.smartlocker.ui.create_update_item.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Model
import com.lock.smartlocker.databinding.ItemModelCreateBinding
import com.lock.smartlocker.ui.create_update_item.ItemViewModel
import com.xwray.groupie.databinding.BindableItem

class ModelItem(
    private val model: Model,
    private val viewModel: ItemViewModel
) : BindableItem<ItemModelCreateBinding>() {

    override fun getLayout() = R.layout.item_model_create

    override fun bind(viewBinding: ItemModelCreateBinding, position: Int) {
        viewBinding.model = model
        model.isSelected = viewModel.modelSelected.value == model.modelId
        viewBinding.root.setOnClickListener {
            viewModel.selectModel(model)
        }
    }
}