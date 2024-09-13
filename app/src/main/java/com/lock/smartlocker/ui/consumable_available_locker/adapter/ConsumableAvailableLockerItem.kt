package com.lock.smartlocker.ui.consumable_available_locker.adapter

import android.content.Context
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.databinding.ItemConsumableAvailableLockerBinding
import com.lock.smartlocker.ui.consumable_available_locker.ConsumableAvailableLockerViewModel
import com.xwray.groupie.databinding.BindableItem

class ConsumableAvailableLockerItem(
    private val context: Context,
    private val model: LockerConsumable,
    private val viewModel: ConsumableAvailableLockerViewModel

) : BindableItem<ItemConsumableAvailableLockerBinding>() {

    override fun getLayout() = R.layout.item_consumable_available_locker

    override fun bind(viewBinding: ItemConsumableAvailableLockerBinding, position: Int) {
        viewBinding.model = model
        when(model.lockerSize){
            "S" -> {
                model.lockerSize = context.getString(R.string.locker_s)
                viewBinding.tvCurrentQuantity.setTextColor(context.getColorStateList(R.color.colorGreen))
                viewBinding.tvQuantityLable.setTextColor(context.getColorStateList(R.color.colorGreen))
            }
            "M" -> {
                model.lockerSize = context.getString(R.string.locker_m)
                viewBinding.tvCurrentQuantity.setTextColor(context.getColorStateList(R.color.colorBlue))
                viewBinding.tvQuantityLable.setTextColor(context.getColorStateList(R.color.colorBlue))
            }
            "L" -> {
                model.lockerSize = context.getString(R.string.locker_l)
                viewBinding.tvCurrentQuantity.setTextColor(context.getColorStateList(R.color.colorOrange))
                viewBinding.tvQuantityLable.setTextColor(context.getColorStateList(R.color.colorOrange))
            }
        }
        viewBinding.root.setOnClickListener {
            viewModel.selectLocker(model)
        }
    }
}