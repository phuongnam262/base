package com.lock.smartlocker.ui.manage_locker.adapter

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Locker
import com.lock.smartlocker.databinding.ItemLockerBinding
import com.lock.smartlocker.ui.manage_locker.ManageLockerViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.databinding.BindableItem

class LockerItem(
    private val context: Context,
    private val model: Locker,
    private val viewModel: ManageLockerViewModel,
    private val fragmentManager: FragmentManager

) : BindableItem<ItemLockerBinding>() {

    override fun getLayout() = R.layout.item_locker

    override fun bind(viewBinding: ItemLockerBinding, position: Int) {
        viewBinding.locker = model
        val info = "Block: ${model.blockName} | Board: ${model.boardAddress} | Address: ${model.hardwareAddress}"
        viewBinding.tvLockerInfo.text = info
        when(model.lockerStatus){
            1 -> {
                viewBinding.tvStatus.text = context.getString(R.string.disabled_status)
                viewBinding.tvStatus.setTextColor(context.getColor(R.color.grey7A7A7A))
            }
            2 -> {
                viewBinding.tvStatus.text = context.getString(R.string.available_status)
                viewBinding.tvStatus.setTextColor(context.getColor(R.color.colorGreen))
            }
            3 -> {
                viewBinding.tvStatus.text = context.getString(R.string.occupied_status)
                viewBinding.tvStatus.setTextColor(context.getColor(R.color.colorBlue))
            }
        }
        viewBinding.btnDisable.setOnClickListener {
            viewModel.lockerSelectedId = model.lockerId
            viewModel.isDisable = true
            val dialog = CustomConfirmDialog.newInstance(
                message = context.getString(R.string.dialog_disable),
            )
            dialog.show(fragmentManager, ConstantUtils.DISABLE_LOCKER)
        }
        viewBinding.btnEnable.setOnClickListener {
            viewModel.lockerSelectedId = model.lockerId
            viewModel.isDisable = false
            val dialog = CustomConfirmDialog.newInstance(
                message = context.getString(R.string.dialog_enable),
            )
            dialog.show(fragmentManager, ConstantUtils.ENABLE_LOCKER)
        }
        viewBinding.btnLocker.setOnClickListener {
            viewModel.openLocker(model.lockerId)
        }
    }
}