package com.lock.smartlocker.ui.deposit_consumable.adapter

import androidx.core.widget.addTextChangedListener
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

        viewBinding.etQuantity.addTextChangedListener {

        }

        viewBinding.btnMax.setOnClickListener {
            if (model.setPoint > model.currentQuantity) {
                if (((model.inputQuantity)?.toInt() ?: 0) < model.setPoint) {
                    val max = model.setPoint - model.currentQuantity
                    viewBinding.etQuantity.setText(max)
                }
            }
        }

        viewBinding.btnReport.setOnClickListener {

        }
    }
}