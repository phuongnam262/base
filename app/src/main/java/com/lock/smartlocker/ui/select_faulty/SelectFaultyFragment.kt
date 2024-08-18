package com.lock.smartlocker.ui.select_faulty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.databinding.FragmentInputSerialNumberBinding
import com.lock.smartlocker.databinding.FragmentSelectFaultyBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberListener
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModel
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModelFactory
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

    private fun initView(){
        viewModel.startTimer()
        viewModel.titlePage.postValue(getString(R.string.select_a_faulty))
        viewModel.spinnerItems.observe(viewLifecycleOwner) { items ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mViewDataBinding?.spiFaultyReason?.adapter = adapter
            viewModel.faultyReason.value = items[0]
        }
        // Retrieve the modelId from the arguments
        val categoryId = arguments?.getString("category_id")

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
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {
                navigateToSelectAvailableLockerFragment()
            }
        }
    }

    fun navigateToSelectAvailableLockerFragment() {
        val returnItemRequest = arguments?.getSerializable("return_item_request") as? ReturnItemRequest
        returnItemRequest?.reason_faulty = viewModel.faultyReason.value
        val bundle = Bundle().apply {
            putSerializable( "return_item_request", returnItemRequest)
        }
        navigateTo(R.id.action_selectFaultyFragment_to_selectAvailableLockerFragment, bundle)
    }
}