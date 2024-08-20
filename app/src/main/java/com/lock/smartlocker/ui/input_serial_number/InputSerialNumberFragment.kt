package com.lock.smartlocker.ui.input_serial_number

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
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
        const val CONFIRMATION_DIALOG_TAG = "confirmation_dialog"
        const val TYPE_INPUT_SERIAL = "type_input_serial"
    }


    override val kodein by kodein()
    private val factory: InputSerialNumberViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_input_serial_number
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: InputSerialNumberViewModel
        get() = ViewModelProvider(this, factory)[InputSerialNumberViewModel::class.java]

    private var isReturnFlow = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scanSerialNumberListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_or_enter_serial_number))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
    }

    private fun initData(){
        if (arguments?.getString(TYPE_INPUT_SERIAL) != null) {
            viewModel.typeInput.value = arguments?.getString(TYPE_INPUT_SERIAL)
            isReturnFlow = false
        }else viewModel.typeInput.value = ConstantUtils.TYPE_RETURN
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {
                val newSerialNumber = viewModel.serialNumber.value
                if (isReturnFlow) {
                    if (viewModel.isItemDetailVisible.value == true && newSerialNumber == viewModel.itemReturnData.value?.serialNumber) {
                        val dialog = CustomConfirmDialog.newInstance(
                            message = getString(R.string.dialog_attention),
                        )
                        dialog.show(childFragmentManager, CONFIRMATION_DIALOG_TAG)
                    } else {
                        viewModel.getItemReturn()
                    }
                } else {
                    if (viewModel.isItemDetailVisible.value == true && newSerialNumber?.lowercase() == viewModel.itemReturnData.value?.serialNumber?.lowercase()) {
                        navigateToSelectAvailableLockerFragment()
                    } else {
                        viewModel.getItemTopup()
                    }
                }
            }
        }
    }


    override fun onDialogPositiveClick(dialogTag: String?) {
        navigateToSelectFaultyFragment(viewModel.itemReturnData.value!!.categoryId)
    }

    override fun onDialogNegativeClick(dialogTag: String?) {
        navigateToSelectAvailableLockerFragment()
    }

    private fun navigateToSelectFaultyFragment(categoryId: String) {
        val returnItemRequest = ReturnItemRequest(
            serial_number = viewModel.serialNumber.value,
            isFaulty = true,
        )
        val bundle = Bundle().apply {
            putString(CATEGORY_ID_KEY, categoryId)
            putSerializable(RETURN_ITEM_REQUEST_KEY, returnItemRequest)
        }
        navigateTo(R.id.action_inputSerialNumberFragment_to_selectFaultyFragment, bundle)
    }

    private fun navigateToSelectAvailableLockerFragment() {
        val returnItemRequest = ReturnItemRequest(
            serial_number = viewModel.serialNumber.value,
            isFaulty = false,
        )
        val bundle = Bundle().apply {
            putSerializable( RETURN_ITEM_REQUEST_KEY, returnItemRequest)
        }
        if(isReturnFlow)
            navigateTo(R.id.action_inputSerialNumberFragment_to_selectAvailableLockerFragment, bundle)
        else navigateTo(R.id.action_inputSerialNumberFragment2_to_selectAvailableLockerFragment2, bundle)
    }

}