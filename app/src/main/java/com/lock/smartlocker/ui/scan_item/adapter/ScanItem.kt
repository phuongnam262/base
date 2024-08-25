package com.lock.smartlocker.ui.scan_item.adapter

import android.content.Context
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.databinding.ItemScanBinding
import com.lock.smartlocker.ui.scan_item.ScanItemViewModel
import com.xwray.groupie.databinding.BindableItem

class ScanItem(
    private val context: Context,
    private val lockerInfo: LockerInfo,
    private val viewModel: ScanItemViewModel,
    ) : BindableItem<ItemScanBinding>() {

    override fun getLayout() = R.layout.item_scan

    override fun bind(viewBinding: ItemScanBinding, position: Int) {
        viewBinding.lockerInfo = lockerInfo
        when (lockerInfo.scanValue) {
            0 -> {
                lockerInfo.scanStatus = context.getString(R.string.status_unscanned)
                viewBinding.tvScanStatus.setTextColor(context.getColorStateList(R.color.colorBlack))
            }
            1 -> {
                lockerInfo.scanStatus = context.getString(R.string.status_scanned)
                viewBinding.tvScanStatus.setTextColor(context.getColorStateList(R.color.green009C2C))
            }
            2 -> {
                lockerInfo.scanStatus = context.getString(R.string.status_not_found)
                viewBinding.tvScanStatus.setTextColor(context.getColorStateList(R.color.colorOrange))
            }
        }
        viewBinding.btnNotFound.setOnClickListener {
            lockerInfo.scanValue = 2
            viewModel.listLockerInfo.value = viewModel.listLockerInfo.value?.map {
                if (it.lockerId == lockerInfo.lockerId) lockerInfo else it
            }
        }
    }
}