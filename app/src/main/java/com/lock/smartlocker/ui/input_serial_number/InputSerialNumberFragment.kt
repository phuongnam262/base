package com.lock.smartlocker.ui.input_serial_number

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.databinding.FragmentInputSerialNumberBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class InputSerialNumberFragment : BaseFragment<FragmentInputSerialNumberBinding, InputSerialNumberViewModel>(),
    KodeinAware,
    View.OnClickListener,
    InputSerialNumberListener,
    CustomConfirmDialog.ConfirmationDialogListener{

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

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_or_enter_serial_number))

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
                val newSerialNumber = viewModel.serialNumber.value
                if (viewModel.isItemDetailVisible.value == true && newSerialNumber == viewModel.itemReturnData.value?.serialNumber) {
                    val dialog = CustomConfirmDialog.newInstance(
                        message = "Does this item require attention?",
                    )
                    dialog.show(childFragmentManager, "confirmation_dialog")
                } else {
                    viewModel.getItemReturn()
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

    fun navigateToSelectFaultyFragment(categoryId: String) {
        val returnItemRequest = ReturnItemRequest(
            serial_number = viewModel.serialNumber.value,
            locker_id = "",
            isFaulty = true,
            reason_faulty = ""
        )
        val bundle = Bundle().apply {
            putString("category_id", categoryId)
            putSerializable( "return_item_request", returnItemRequest)
        }
        navigateTo(R.id.action_inputSerialNumberFragment_to_selectFaultyFragment, bundle)
    }

    fun navigateToSelectAvailableLockerFragment() {
        val returnItemRequest = ReturnItemRequest(
            serial_number = viewModel.serialNumber.value,
            locker_id = "",
            isFaulty = false,
            reason_faulty = ""
        )
        val bundle = Bundle().apply {
            putSerializable( "return_item_request", returnItemRequest)
        }
        navigateTo(R.id.action_inputSerialNumberFragment_to_selectAvailableLockerFragment, bundle)
    }

}