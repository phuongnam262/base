package com.lock.smartlocker.ui.select_available_locker.adapter

import android.content.Context
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.databinding.ItemAvailableLockerBinding
import com.lock.smartlocker.ui.select_available_locker.SelectAvailableLockerViewModel
import com.xwray.groupie.databinding.BindableItem

class AvailableLockerItem(
    private val context: Context,
    private val model: Locker,
    private val viewModel: SelectAvailableLockerViewModel

) : BindableItem<ItemAvailableLockerBinding>() {

    override fun getLayout() = R.layout.item_available_locker

    override fun bind(viewBinding: ItemAvailableLockerBinding, position: Int) {
        viewBinding.model = model
        viewBinding.isSelected = viewModel.selectedLocker.value == model
        when(model.lockerSizeKey){
            "S" -> model.lockerSizeKey = context.getString(R.string.locker_s)
            "M" -> model.lockerSizeKey = context.getString(R.string.locker_m)
            "L" -> model.lockerSizeKey = context.getString(R.string.locker_l)
        }
        viewBinding.root.setOnClickListener {
            viewModel.selectLocker(model)
        }
    }
}