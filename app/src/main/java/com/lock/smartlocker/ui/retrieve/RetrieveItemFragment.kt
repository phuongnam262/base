package com.lock.smartlocker.ui.retrieve

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentRetrieveItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category.adapter.ModelItem
import com.lock.smartlocker.ui.retrieve.adapter.CategoryRetrieveItem
import com.lock.smartlocker.ui.retrieve.adapter.RetrieveItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class RetrieveItemFragment : BaseFragment<FragmentRetrieveItemBinding, RetrieveViewModel>(),
    KodeinAware,
    View.OnClickListener {

    override val kodein by kodein()
    private val factory: RetrieveViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_retrieve_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    private val retrieveAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: RetrieveViewModel
        get() = ViewModelProvider(this, factory)[RetrieveViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    @SuppressLint("SetTextI18n")
    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.retrieve_item))
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.button_retrieve_all)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvModels?.adapter = retrieveAdapter

        viewModel.categoriesRetrieve.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryRetrieveItem(it, viewModel)
            })
            if (categories.isNotEmpty())
                viewModel.onCategorySelected(categories[0])
        }

        viewModel.retrieveModels.observe(viewLifecycleOwner) { models ->
            retrieveAdapter.update(models.map { RetrieveItem(it, viewModel) })
        }

        viewModel.categoryIdSelected.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            Log.e("RetrieveItemFragment", "onClick: ${v?.id}")
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.btn_process -> {

                }
            }
        }else Log.e("RetrieveItemFragment", "onClick: false")
    }
}