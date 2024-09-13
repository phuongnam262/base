package com.lock.smartlocker.ui.deposit_consumable.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ConsumableInLocker
import com.lock.smartlocker.databinding.ItemConsumableTopupBinding
import com.lock.smartlocker.ui.deposit_consumable.DepositConsumableViewModel
import com.xwray.groupie.databinding.BindableItem

class ConsumableTopupItem(
    private val model: ConsumableInLocker,
    private val viewModel: DepositConsumableViewModel
) : BindableItem<ItemConsumableTopupBinding>() {

    override fun getLayout() = R.layout.item_consumable_topup

    override fun bind(viewBinding: ItemConsumableTopupBinding, position: Int) {
        viewBinding.consumable = model

        viewBinding.btnMax.setOnClickListener {
            if (model.setPoint > model.currentQuantity) {
                val max = model.setPoint - model.currentQuantity
                viewBinding.etQuantity.setText(max.toString())
            }else viewBinding.etQuantity.setText("0")
        }

        viewBinding.btnReport.setOnClickListener {

        }
    }
}