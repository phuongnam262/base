package com.lock.smartlocker.ui.cart.adapter

import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ItemCartBinding
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.ui.cart.CartViewModel
import com.xwray.groupie.databinding.BindableItem

class CartItem(
    private val cartItem: CartItem,
    private val viewModel: CartViewModel

) : BindableItem<ItemCartBinding>() {

    override fun getLayout() = R.layout.item_cart

    override fun bind(viewBinding: ItemCartBinding, position: Int) {
        viewBinding.cartItem = cartItem

        viewBinding.btnIncrease.setOnClickListener {
            viewModel.increaseQuantity(cartItem)
        }

        viewBinding.btnDecrease.setOnClickListener {
            viewModel.decreaseQuantity(cartItem)
        }
    }
}

