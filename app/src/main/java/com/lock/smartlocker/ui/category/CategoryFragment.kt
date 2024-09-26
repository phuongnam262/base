package com.lock.smartlocker.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentCategoryBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category.adapter.CategoryItem
import com.lock.smartlocker.ui.category.adapter.ModelItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryViewModel>(),
    KodeinAware,
    View.OnClickListener {

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_category
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    private val modelAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CategoryViewModel
        get() = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    companion object{
        var listCartItem: ArrayList<CartItem>? = null
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
        viewModel.categories.removeObservers(viewLifecycleOwner)
        viewModel.availableModels.removeObservers(viewLifecycleOwner)
        viewModel.categoryIdSelected.removeObservers(viewLifecycleOwner)
        viewModel.listCartItem.removeObservers(viewLifecycleOwner)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData(){
        if (listCartItem == null) {
            listCartItem = ArrayList()
        }
        viewModel.listCartItem.value = listCartItem
        if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
            if (arguments?.getString(ConstantUtils.TYPE_OPEN) == ConstantUtils.TYPE_LOAN) {
                viewModel.loadAvailableItem(1)
            } else {
                viewModel.loadAvailableItem(2)
            }
        }

        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvModels?.adapter = modelAdapter

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
            if (categories.isNotEmpty()) {
                viewModel.onCategorySelected(categories[0])
            }
        }

        viewModel.availableModels.observe(viewLifecycleOwner) { models ->
            modelAdapter.update(models.map { ModelItem(it, viewModel) })
        }

        viewModel.categoryIdSelected.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.listCartItem.observe(viewLifecycleOwner) {
            modelAdapter.notifyDataSetChanged()
            listCartItem = it
            if (it.size == 0) {
                mViewDataBinding?.bottomMenu?.rlCart?.isClickable = false
                mViewDataBinding?.bottomMenu?.rlCart?.alpha = 0.3f
            }else{
                mViewDataBinding?.bottomMenu?.rlCart?.isClickable = true
                mViewDataBinding?.bottomMenu?.rlCart?.alpha = 1f
            }
        }


        viewModel.availableItem.observe(viewLifecycleOwner) {
            viewModel.updateAvailableModels()
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
                    val bundle = Bundle().apply {
                        putString(
                            ConstantUtils.TYPE_OPEN,
                            arguments?.getString(ConstantUtils.TYPE_OPEN)
                        )
                    }
                    navigateTo(R.id.action_categoryFragment_to_cartFragment, bundle)
                }
            }
        }
    }
}