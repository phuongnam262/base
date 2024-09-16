package com.lock.smartlocker.ui.cart_consumable.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.FragmentManager
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.CartConsumableItem
import com.lock.smartlocker.databinding.ItemCartConsumableBinding
import com.lock.smartlocker.ui.cart_consumable.CartConsumableViewModel
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.databinding.BindableItem

class CartItem(
    private val cartItem: CartConsumableItem,
    private val viewModel: CartConsumableViewModel,

) : BindableItem<ItemCartConsumableBinding>() {

    override fun getLayout() = R.layout.item_cart_consumable

    override fun bind(viewBinding: ItemCartConsumableBinding, position: Int) {
        viewBinding.cartItem = cartItem

        viewBinding.btnIncrease.setOnClickListener {
            viewModel.increaseQuantity(cartItem)
        }

        viewBinding.btnDecrease.setOnClickListener {
            viewModel.decreaseQuantity(cartItem)
        }

        viewBinding.etQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    if (s.toString() == "0") viewBinding.etQuantity.setText("1")
                }else viewBinding.etQuantity.setText("1")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}

