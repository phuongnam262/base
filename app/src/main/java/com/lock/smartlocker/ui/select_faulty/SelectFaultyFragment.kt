package com.lock.smartlocker.ui.select_faulty

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.databinding.FragmentSelectFaultyBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class SelectFaultyFragment : BaseFragment<FragmentSelectFaultyBinding, SelectFaultyViewModel>(),
    KodeinAware,
    View.OnClickListener,
    SelectFaultyListener {

    override val kodein by kodein()
    private val factory: SelectFaultyViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_select_faulty
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: SelectFaultyViewModel
        get() = ViewModelProvider(this, factory)[SelectFaultyViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectFaultyListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.selectFaultyListener = null
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.select_a_faulty))
        viewModel.spinnerItems.observe(viewLifecycleOwner) { items ->
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.item_spinner,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mViewDataBinding?.spiFaultyReason?.adapter = adapter
        }

        mViewDataBinding?.spiFaultyReason?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedReason = parent.getItemAtPosition(position) as String
                viewModel.setSelectedFaultyReason(selectedReason)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        // Retrieve the modelId from the arguments
        val categoryId = arguments?.getString(InputSerialNumberFragment.CATEGORY_ID_KEY)

        if (categoryId != null) {
            viewModel.loadSpinnerItems(categoryId)
        }

        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
    }

    private fun initData(){
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.btn_process -> {
                    navigateToSelectAvailableLockerFragment()
                }
            }
        }
    }

    private fun navigateToSelectAvailableLockerFragment() {
        val returnItem = arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
        returnItem?.reasonFaulty = viewModel.selectedFaultyReason.value.toString()
        val bundle = Bundle().apply {
            putSerializable( InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY, returnItem)
        }
        navigateTo(R.id.action_selectFaultyFragment_to_selectAvailableLockerFragment, bundle)
    }
}