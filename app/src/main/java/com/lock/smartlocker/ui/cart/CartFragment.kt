package com.lock.smartlocker.ui.cart

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentCartBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category.CategoryFragment
import com.lock.smartlocker.util.ConstantUtils
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.listCartItem.value = null
        viewModel.listCartItem.removeObservers(viewLifecycleOwner)
        viewModel.listLockerInfo.removeObservers(viewLifecycleOwner)
    }

    private fun initData(){
        mViewDataBinding?.rvCartItems?.adapter = cartAdapter
        viewModel.listCartItem.value = CategoryFragment.listCartItem
        viewModel.listCartItem.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.update(cartItems.map {
                CartItemAdapter(it, viewModel)
            })
            CategoryFragment.listCartItem = cartItems
            viewModel.enableButtonProcess.value = cartItems.size > 0
        }

        viewModel.listLockerInfo.observe(viewLifecycleOwner) {
            val listLockerInfo = it
            if (listLockerInfo != null) {
                val bundle = Bundle().apply {
                    putString(ConstantUtils.TRANSACTION_ID, viewModel.transactionId.value)
                }
                navigateTo(R.id.action_cartFragment_to_collectItemFragment, bundle,)
                CategoryFragment.listCartItem = null
            }
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> {
                    CategoryFragment.listCartItem = null
                    activity?.finish()
                }

                R.id.iv_back -> {
                    activity?.supportFragmentManager?.popBackStack()
                }

                R.id.rl_item -> {}
                R.id.btn_process -> {
                    if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
                        if (arguments?.getString(ConstantUtils.TYPE_OPEN) == ConstantUtils.TYPE_LOAN) {
                            viewModel.createInventoryTransaction(1)
                        } else {
                            viewModel.createInventoryTransaction(2)
                        }
                    }
                }
            }
        }
    }
}