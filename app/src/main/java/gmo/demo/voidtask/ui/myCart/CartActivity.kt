package gmo.demo.voidtask.ui.myCart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.repositories.CartRepository
import gmo.demo.voidtask.databinding.ActivityCartBinding
import gmo.demo.voidtask.ui.base.BaseActivity

class CartActivity : BaseActivity<ActivityCartBinding, CartViewModel>() {

    private lateinit var cartAdapter: CartAdapter
    private val repository = CartRepository()

    override val viewModel: CartViewModel by viewModels {
        CartViewModelFactory(repository = repository)
    }

    override val layoutId: Int
        get() = R.layout.activity_cart

    override val bindingVariable: Int
        get() = BR.viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            cartItems = emptyList(),
            onQuantityChanged = { cartItem, newQuantity ->
                viewModel.updateQuantity(cartItem.id, newQuantity)
            },
            onRemoveItem = { cartItem ->
                viewModel.removeFromCart(cartItem.id)
            }
        )

        mViewDataBinding?.recCartItems?.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }
    }

    private fun setupObservers() {
        viewModel.cartItems.observe(this) { items ->
            cartAdapter = CartAdapter(
                cartItems = items,
                onQuantityChanged = { cartItem, newQuantity ->
                    viewModel.updateQuantity(cartItem.id, newQuantity)
                },
                onRemoveItem = { cartItem ->
                    viewModel.removeFromCart(cartItem.id)
                }
            )
            mViewDataBinding?.recCartItems?.adapter = cartAdapter
            
            // Hiển thị/ẩn layout giỏ hàng trống
            if (items.isEmpty()) {
                mViewDataBinding?.layoutEmptyCart?.visibility = android.view.View.VISIBLE
                mViewDataBinding?.layoutCartContent?.visibility = android.view.View.GONE
            } else {
                mViewDataBinding?.layoutEmptyCart?.visibility = android.view.View.GONE
                mViewDataBinding?.layoutCartContent?.visibility = android.view.View.VISIBLE
            }
        }

        viewModel.totalPrice.observe(this) { total ->
            mViewDataBinding?.tvTotalPrice?.text = "$${String.format("%.2f", total)}"
        }

        viewModel.totalItems.observe(this) { total ->
            mViewDataBinding?.tvTotalItems?.text = getString(R.string.total_items, total)
        }
    }

    private fun setupClickListeners() {
        mViewDataBinding?.btnClearCart?.setOnClickListener {
            viewModel.clearCart()
            Toast.makeText(this, getString(R.string.cart_cleared), Toast.LENGTH_SHORT).show()
        }

        mViewDataBinding?.btnCheckout?.setOnClickListener {
            Toast.makeText(this, getString(R.string.checkout_developing), Toast.LENGTH_SHORT).show()
        }
    }
}
