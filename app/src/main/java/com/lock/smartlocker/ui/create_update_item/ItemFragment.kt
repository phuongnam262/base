package com.lock.smartlocker.ui.create_update_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.databinding.FragmentItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.create_update_item.adapter.CategoryItem
import com.lock.smartlocker.ui.create_update_item.adapter.ModelItem
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ItemFragment : BaseFragment<FragmentItemBinding, ItemViewModel>(),
    KodeinAware,
    View.OnClickListener,
    ItemListener {

    override val kodein by kodein()
    private val factory: ItemViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: ItemViewModel
        get() = ViewModelProvider(this, factory)[ItemViewModel::class.java]

    private val categoryAdapter = GroupAdapter<GroupieViewHolder>()
    private val modelAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.itemListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.itemListener = null
        viewModel.categories.removeObservers(viewLifecycleOwner)
        viewModel.models.removeObservers(viewLifecycleOwner)
        viewModel.categorySelected.removeObservers(viewLifecycleOwner)
        viewModel.modelSelected.removeObservers(viewLifecycleOwner)
    }

    private fun initView() {
        viewModel.titlePage.postValue(getString(R.string.create_new_item))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.create_button)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.item_type_options,
            R.layout.item_spinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mViewDataBinding?.spiItemType?.adapter = adapter
        mViewDataBinding?.spiItemType?.setSelection(0)
        mViewDataBinding?.spiItemType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.itemType = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Do nothing
                }
            }
        mViewDataBinding?.rvCategories?.adapter = categoryAdapter
        mViewDataBinding?.rvModels?.adapter = modelAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initData() {
        viewModel.itemReturn.value?.serialNumber = arguments?.getString(ConstantUtils.SERIAL_NUMBER)!!
        viewModel.serialNumber.value = arguments?.getString(ConstantUtils.SERIAL_NUMBER)
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
            if (categories.isNotEmpty()) {
                viewModel.onCategorySelected(categories[0])
            }
        }

        viewModel.models.observe(viewLifecycleOwner) { models ->
            modelAdapter.update(models.map { ModelItem(it, viewModel) })
        }

        viewModel.categorySelected.observe(viewLifecycleOwner) {
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.modelSelected.observe(viewLifecycleOwner) {
            modelAdapter.notifyDataSetChanged()
            viewModel.enableButtonProcess.value = true
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.supportFragmentManager?.popBackStack()
                R.id.btn_process -> {
                    viewModel.createItem()
                }
            }
        }
    }

    private fun navigateToSelectAvailableLockerFragment() {
//        val returnItem =
//            arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
//        returnItem?.reasonFaulty = viewModel.selectedFaultyReason.value.toString()
//        val bundle = Bundle().apply {
//            putSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY, returnItem)
//        }
//        navigateTo(R.id.action_selectFaultyFragment_to_selectAvailableLockerFragment, bundle)
    }

    override fun handleSuccess() {

    }
}