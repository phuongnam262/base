package com.lock.smartlocker.ui.input_serial_number

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentInputSerialNumberBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class InputSerialNumberFragment : BaseFragment<FragmentInputSerialNumberBinding, InputSerialNumberViewModel>(),
    KodeinAware,
    View.OnClickListener,
    InputSerialNumberListener,
    CustomConfirmDialog.ConfirmationDialogListener{

    companion object {
        const val CATEGORY_ID_KEY = "category_id"
        const val RETURN_ITEM_REQUEST_KEY = "return_item_request"
        const val TYPE_INPUT_SERIAL = "type_input_serial"
    }


    override val kodein by kodein()
    private val factory: InputSerialNumberViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_input_serial_number
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: InputSerialNumberViewModel
        get() = ViewModelProvider(this, factory)[InputSerialNumberViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scanSerialNumberListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scanSerialNumberListener = null
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_or_enter_serial_number))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.containerItem?.btnUpdate?.setOnClickListener(this)
        viewModel.enableButtonProcess.value = true
    }

    private fun initData(){
        if (arguments?.getString(TYPE_INPUT_SERIAL) != null) {
            viewModel.typeInput.value = arguments?.getString(TYPE_INPUT_SERIAL)
        }else {
            viewModel.isReturnFlow = true
            viewModel.typeInput.value = ConstantUtils.TYPE_RETURN
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> {
                    viewModel.serialNumber.value = null
                    viewModel.showStatusText.value = false
                    viewModel.isItemDetailVisible.value = false
                    viewModel.isCreateItem.value = false
                    viewModel.isUpdateItem.value = false
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
                R.id.btn_process -> {
                    val newSerialNumber = viewModel.serialNumber.value
                    if (viewModel.isReturnFlow) {
                        if (newSerialNumber?.lowercase() != viewModel.itemReturnData.value?.serialNumber?.lowercase()
                            || viewModel.itemReturnData.value == null){
                            viewModel.getItemReturn()
                        }else if (viewModel.isItemDetailVisible.value == true && newSerialNumber?.lowercase() == viewModel.itemReturnData.value?.serialNumber?.lowercase()) {
                            showDialogConfirm(getString(R.string.dialog_attention))
                        }
                    } else {
                        if (newSerialNumber?.lowercase() != viewModel.itemReturnData.value?.serialNumber?.lowercase()
                            || viewModel.itemReturnData.value == null){

                            viewModel.getItemTopup()
                        }else if (viewModel.isItemDetailVisible.value == true && viewModel.itemReturnData.value?.modelId != "") {
                            navigateToSelectAvailableLockerFragment()
                        } else {
                            if (viewModel.isItemDetailVisible.value == true && viewModel.itemReturnData.value?.modelId == "") {
                                navigateToItemFragment()
                            }
                        }
                    }
                }
                R.id.btn_update -> navigateToItemFragment()
            }
        }
    }


    override fun onDialogConfirmClick(dialogTag: String?) {
        navigateToSelectFaultyFragment(viewModel.itemReturnData.value!!.categoryId)
    }

    override fun onDialogCancelClick() {
        navigateToSelectAvailableLockerFragment()
    }

    private fun navigateToSelectFaultyFragment(categoryId: String) {
        val bundle = Bundle().apply {
            putString(CATEGORY_ID_KEY, categoryId)
            putSerializable(RETURN_ITEM_REQUEST_KEY, viewModel.itemReturnData.value)
        }
        navigateTo(R.id.action_inputSerialNumberFragment_to_selectFaultyFragment2, bundle)
    }

    private fun navigateToSelectAvailableLockerFragment() {
        val bundle = Bundle().apply {
            putSerializable( RETURN_ITEM_REQUEST_KEY, viewModel.itemReturnData.value)
            putString( TYPE_INPUT_SERIAL, viewModel.typeInput.value)
        }
        if(viewModel.isReturnFlow)
            navigateTo(R.id.action_inputSerialNumberFragment_to_selectAvailableLockerFragment, bundle)
        else navigateTo(R.id.action_inputSerialNumberFragment2_to_selectAvailableLockerFragment2, bundle)
    }

    private fun navigateToItemFragment() {
        val bundle = Bundle().apply {
            putSerializable(ConstantUtils.SERIAL_NUMBER, viewModel.serialNumber.value)
        }
        navigateTo(R.id.action_inputSerialNumberFragment2_to_itemFragment, bundle)
    }

}