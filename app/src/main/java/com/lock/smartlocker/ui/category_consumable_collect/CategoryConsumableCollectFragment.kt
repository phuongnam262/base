package com.lock.smartlocker.ui.category_consumable_collect

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.CartConsumableItem
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentCategoryConsumableCollectBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category_consumable_collect.adapter.CategoryItem
import com.lock.smartlocker.ui.category_consumable_collect.adapter.ConsumableItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CategoryConsumableCollectFragment : BaseFragment<FragmentCategoryConsumableCollectBinding, CategoryConsumableCollectViewModel>(),
    KodeinAware,
    View.OnClickListener {

    override val kodein by kodein()
    private val factory: CategoryConsumableCollectViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_category_consumable_collect
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    private val consumableAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CategoryConsumableCollectViewModel
        get() = ViewModelProvider(this, factory)[CategoryConsumableCollectViewModel::class.java]

    companion object{
        var listCartItem: ArrayList<CartConsumableItem>? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlItem?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlCart?.setOnClickListener(this)
        mViewDataBinding?.tvHelloSomething?.text = "Hello ${PreferenceHelper.getString(ConstantUtils.ADMIN_NAME, "Admin")}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.consumableAvailableItems.removeObservers(viewLifecycleOwner)
        viewModel.availableConsumables.removeObservers(viewLifecycleOwner)
        viewModel.categoryIdSelected.removeObservers(viewLifecycleOwner)
        viewModel.listCartItem.removeObservers(viewLifecycleOwner)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        if (listCartItem == null) {
            listCartItem = ArrayList()
        }
        viewModel.listCartItem.value = listCartItem
        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvConsumables?.adapter = consumableAdapter

        viewModel.consumableAvailableItems.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
        }

        viewModel.availableConsumables.observe(viewLifecycleOwner) { models ->
            consumableAdapter.update(models.map { ConsumableItem(it, viewModel) })
        }

        viewModel.categoryIdSelected.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.listCartItem.observe(viewLifecycleOwner) {
            consumableAdapter.notifyDataSetChanged()
            listCartItem = it
            if (it.size == 0) {
                mViewDataBinding?.bottomMenu?.rlCart?.isClickable = false
                mViewDataBinding?.bottomMenu?.rlCart?.alpha = 0.3f
            }else{
                mViewDataBinding?.bottomMenu?.rlCart?.isClickable = true
                mViewDataBinding?.bottomMenu?.rlCart?.alpha = 1f
            }
        }

        viewModel.consumableAvailableItems.observe(viewLifecycleOwner) {
            viewModel.updateAvailableConsumable()
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> {
                    listCartItem = null
                    activity?.finish()
                }
                R.id.rl_item -> {}
                R.id.rl_cart -> {
                    navigateTo(R.id.action_categoryConsumableCollectFragment_to_cartConsumableFragment, null)
                }
            }
        }
    }
}