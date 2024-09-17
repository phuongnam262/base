package com.lock.smartlocker.ui.collect_consumable_item.adapter

import android.content.Context
import android.text.Html
import androidx.fragment.app.FragmentManager
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerInfoCollect
import com.lock.smartlocker.databinding.ItemCollectConsumableBinding
import com.lock.smartlocker.ui.collect_consumable_item.CollectConsumableItemViewModel
import com.lock.smartlocker.util.view.custom.ReportStockDialog
import com.xwray.groupie.databinding.BindableItem

class CollectItem(
    private val context: Context,
    private val lockerInfo: LockerInfoCollect,
    private val viewModel: CollectConsumableItemViewModel,
    private val fragmentManager: FragmentManager

    ) : BindableItem<ItemCollectConsumableBinding>() {

    override fun getLayout() = R.layout.item_collect_consumable

    override fun bind(viewBinding: ItemCollectConsumableBinding, position: Int) {
        viewBinding.lockerInfo = lockerInfo
        viewBinding.viewmodel = viewModel
        viewBinding.tvQuantity.text = Html.fromHtml(context.getString(R.string.take_item,
            lockerInfo.takeNumber.toString()), Html.FROM_HTML_MODE_COMPACT)
        viewBinding.btnReopen.setOnClickListener {
            viewModel.reopenLocker(lockerInfo.lockerId)
        }
        viewBinding.btnReport.setOnClickListener {
            val dialog = ReportStockDialog.newInstance(lockerInfo.lockerId, lockerInfo.consumableId)
            dialog.show(fragmentManager, "ReportStockDialog")
        }
    }
}