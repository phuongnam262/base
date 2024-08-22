package com.lock.smartlocker.ui.collect_items.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.databinding.ItemCollectBinding
import com.lock.smartlocker.ui.collect_items.CollectItemViewModel
import com.xwray.groupie.databinding.BindableItem

class CollectItem(
    private val lockerInfo: LockerInfo,
    private val viewModel: CollectItemViewModel,

    ) : BindableItem<ItemCollectBinding>() {

    override fun getLayout() = R.layout.item_collect

    override fun bind(viewBinding: ItemCollectBinding, position: Int) {
        viewBinding.lockerInfo = lockerInfo
        viewBinding.btnReopen.setOnClickListener {
            viewModel.reopenLocker(lockerInfo.lockerId)
        }
    }
}