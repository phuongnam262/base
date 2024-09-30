package com.lock.smartlocker.ui.create_update_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.Category
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.databinding.FragmentItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.create_update_item.adapter.CategoryItem
import com.lock.smartlocker.ui.create_update_item.adapter.ModelItem
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment.Companion.TYPE_INPUT_SERIAL
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment.Companion.TYPE_UPDATE
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
        mViewDataBinding?.containerItem?.btnUpdate?.setOnClickListener(this)

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
        viewModel.isUpdateFlow.value = arguments?.getString(TYPE_UPDATE) != null
        if (viewModel.isUpdateFlow.value == false) {
            val itemReturn = ItemReturn(
                transactionId = "",
                serialNumber = arguments?.getString(ConstantUtils.SERIAL_NUMBER)!!,
                modelName = "",
                modelId = "",
                categoryName = "",
                categoryId = "",
                loaneeEmail = "",
                modelImage = "null",
                type = 0,
                lockerId = "",
                reasonFaulty = ""
            )
            viewModel.itemReturn.value = itemReturn
        }else{
            viewModel.itemReturn.value = arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
        }
        viewModel.serialNumber.value = arguments?.getString(ConstantUtils.SERIAL_NUMBER)
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.update(categories.map {
                CategoryItem(it, viewModel)
            })
            if (categories.isNotEmpty()) {
                if (viewModel.isUpdateFlow.value == false) viewModel.onCategorySelected(categories[0])
                else scrollToAndClickCategory(viewModel.itemReturn.value?.categoryId)
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

        if (viewModel.isUpdateFlow.value == true) {
            loadDataUpdate()
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> {
                    if (viewModel.typeCreateItem.value == 2 && viewModel.isUpdateFlow.value == false) createSuccess()
                    else activity?.supportFragmentManager?.popBackStack()
                }
                R.id.btn_process -> {
                    when(viewModel.typeCreateItem.value){
                        0 -> viewModel.createItem()
                        1 -> navigateToSelectAvailableLockerFragment()
                        2 -> viewModel.updateItem()
                    }
                }
                R.id.btn_update -> {
                    updateItem()
                }
            }
        }
    }

    private fun navigateToSelectAvailableLockerFragment() {
        val bundle = Bundle().apply {
            putString( TYPE_INPUT_SERIAL, arguments?.getString(TYPE_INPUT_SERIAL))
            putSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY, viewModel.itemReturn.value)
        }
        navigateTo(R.id.action_itemFragment_to_selectAvailableLockerFragment2, bundle)
    }


    override fun handleSuccess() {
        createSuccess()
    }

    private fun createSuccess(){
        viewModel.showInfoItem.value = true
        viewModel.typeCreateItem.value = 1
        viewModel.itemReturn.value = viewModel.itemReturn.value
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.process_button)
        mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE
        viewModel.titlePage.postValue(getString(R.string.item_information))
        mViewDataBinding?.tvHeaderInfo?.text = getString(R.string.item_information_info)
    }

    private fun updateItem(){
        viewModel.typeCreateItem.value = 2
        viewModel.showInfoItem.value = false
        viewModel.itemReturn.value = viewModel.itemReturn.value
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.update_button)
        viewModel.titlePage.postValue(getString(R.string.update_item_information))
        mViewDataBinding?.tvHeaderInfo?.text = getString(R.string.update_item_info)
    }

    private fun loadDataUpdate(){
        updateItem()
        mViewDataBinding?.spiItemType?.setSelection(viewModel.itemReturn.value?.type!! - 1)
        viewModel.modelSelected.value = viewModel.itemReturn.value?.modelId
    }

    private fun scrollToAndClickCategory(categoryId: String?) {
        val position = viewModel.categories.value?.indexOfFirst { it.categoryId == categoryId }
        if (position != -1) {
            Handler(Looper.getMainLooper()).postDelayed({
                position?.let { mViewDataBinding?.rvCategories?.smoothScrollToPosition(it) }
                if (position != null) {
                    viewModel.categories.value?.get(position)?.let { viewModel.onCategorySelected(it) }
                }
            }, 600) // Delay 500 milliseconds
        }
    }
}