package com.lock.smartlocker.ui.cart_consumable

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentCartConsumableBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category_consumable_collect.CategoryConsumableCollectFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import com.lock.smartlocker.ui.cart_consumable.adapter.CartItem as CartItemAdapter

class CartConsumableFragment : BaseFragment<FragmentCartConsumableBinding, CartConsumableViewModel>(),
    KodeinAware,
    View.OnClickListener, CustomConfirmDialog.ConfirmationDialogListener {

    override val kodein by kodein()
    private val factory: CartConsumableViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_cart_consumable
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val cartAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CartConsumableViewModel
        get() = ViewModelProvider(this, factory)[CartConsumableViewModel::class.java]


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
        viewModel.listCartItem.value = CategoryConsumableCollectFragment.listCartItem
        viewModel.listCartItem.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.update(cartItems.map {
                CartItemAdapter(it, viewModel)
            })
            CategoryConsumableCollectFragment.listCartItem = cartItems
            viewModel.enableButtonProcess.value = cartItems.size > 0
        }

        viewModel.listLockerInfo.observe(viewLifecycleOwner) {
            val listLockerInfo = it
            if (listLockerInfo != null) {
                val bundle = Bundle().apply {
                    putString(ConstantUtils.TRANSACTION_ID, viewModel.transactionId.value)
                }
                navigateTo(R.id.action_cartConsumableFragment_to_collectConsumableItemFragment, bundle,)
                CategoryConsumableCollectFragment.listCartItem = null
            }
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> {
                    CategoryConsumableCollectFragment.listCartItem = null
                    activity?.finish()
                }

                R.id.iv_back, R.id.rl_item  -> {
                    activity?.supportFragmentManager?.popBackStack()
                }

                R.id.btn_process -> {
                    viewModel.createInventoryTransaction()
                }
            }
        }
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        TODO("Not yet implemented")
    }

    override fun onDialogCancelClick() {
        TODO("Not yet implemented")
    }
}