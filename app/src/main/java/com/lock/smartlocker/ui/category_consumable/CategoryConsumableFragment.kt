package com.lock.smartlocker.ui.category_consumable

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.databinding.FragmentCategoryConsumableBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category_consumable.adapter.CategoryItem
import com.lock.smartlocker.ui.category_consumable.adapter.ConsumableItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CategoryConsumableFragment : BaseFragment<FragmentCategoryConsumableBinding, CategoryConsumableViewModel>(),
    KodeinAware,
    View.OnClickListener {

    override val kodein by kodein()
    private val factory: CategoryConsumableViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_category_consumable
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    private val consumableAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CategoryConsumableViewModel
        get() = ViewModelProvider(this, factory)[CategoryConsumableViewModel::class.java]

    private var isInnitData = false
    companion object{
        var listLockerSelected = ArrayList<LockerConsumable>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.category))
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        isInnitData = true
        viewModel.getConsumableAvailableItem()
        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvConsumables?.adapter = consumableAdapter

        viewModel.categoriesConsumable.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
            viewModel.enableButtonProcess.value = categories.isNotEmpty()
        }

        viewModel.listConsumable.observe(viewLifecycleOwner) { models ->
            consumableAdapter.update(models.map { ConsumableItem(it, viewModel) })
        }

        viewModel.categoryIdSelected.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.lockers.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                listLockerSelected.clear()
                listLockerSelected.addAll(it)
                openAvailableLocker()
                viewModel.lockers.value = ArrayList()
            }
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun openAvailableLocker(){
        val bundle = Bundle().apply {
            putString(ConstantUtils.CATEGORY_ID, viewModel.categoryIdSelected.value)
        }
        navigateTo(R.id.action_categoryConsumableFragment_to_consumableAvailableLockerFragment, bundle)
    }
}