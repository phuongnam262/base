package com.lock.smartlocker.ui.category

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentCategoryBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.category.adapter.CategoryItem
import com.lock.smartlocker.ui.category.adapter.ModelItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CategoryFragment : BaseFragment<FragmentCategoryBinding, CategoryViewModel>(),
    KodeinAware,
    View.OnClickListener {

    companion object {
        const val CART_ITEMS = "CART_ITEMS"
    }

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_category
    override val bindingVariable: Int
        get() = BR.viewmodel

    val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    val modelAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CategoryViewModel
        get() = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlItem?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlCart?.setOnClickListener(this)
    }

    private fun initData(){
        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvModels?.adapter = modelAdapter

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
        }

        viewModel.availableModels.observe(viewLifecycleOwner) { models ->
            modelAdapter.update(models.map { ModelItem(it, viewModel) })
        }

        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.rl_item -> {}
            R.id.rl_cart -> {

                val cartItemsList = viewModel.cartItems.value
                if (cartItemsList != null) {
                    val bundle = Bundle().apply {
                        putParcelableArrayList(CART_ITEMS, ArrayList(cartItemsList))
                    }
                    navigateTo(R.id.action_categoryFragment_to_cartFragment, bundle)
                }
            }
        }
    }
}