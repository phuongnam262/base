package com.lock.smartlocker.ui.retrieve.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerRetrieve
import com.lock.smartlocker.databinding.ItemRetrieveBinding
import com.lock.smartlocker.ui.retrieve.RetrieveViewModel
import com.xwray.groupie.databinding.BindableItem

class RetrieveItem(
    private val model: LockerRetrieve,
    private val viewModel: RetrieveViewModel
) : BindableItem<ItemRetrieveBinding>() {

    override fun getLayout() = R.layout.item_retrieve

    override fun bind(viewBinding: ItemRetrieveBinding, position: Int) {
        viewBinding.lockerRetrieve = model
        viewBinding.btnLocker.setOnClickListener {
            //viewModel.openLocker(model.lockerId)
        }
    }
}