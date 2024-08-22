package com.lock.smartlocker.ui.cart

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.databinding.FragmentCartBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category.CategoryFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import com.lock.smartlocker.ui.cart.adapter.CartItem as CartItemAdapter

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>(),
    KodeinAware,
    View.OnClickListener {

    override val kodein by kodein()
    private val factory: CartViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_cart
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val cartAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CartViewModel
        get() = ViewModelProvider(this, factory)[CartViewModel::class.java]


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.cart))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlItem?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

    }

    private fun initData(){
        mViewDataBinding?.rvCartItems?.adapter = cartAdapter
        arguments?.let {
            val cartItems = it.getParcelableArrayList<CartItem>(CategoryFragment.CART_ITEMS) ?: emptyList()
            viewModel.setCartItems(cartItems)
        }

        viewModel.cartItems.observe(viewLifecycleOwner) { categories ->
            cartAdapter.update(categories.map {
                CartItemAdapter(it, viewModel)
            })
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.rl_item -> {}
            R.id.btn_process -> {}
        }
    }
}