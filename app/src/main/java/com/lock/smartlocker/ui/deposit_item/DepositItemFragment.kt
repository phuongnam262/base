package com.lock.smartlocker.ui.deposit_item

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.databinding.FragmentDepositItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DepositItemFragment : BaseFragment<FragmentDepositItemBinding, DepositItemViewModel>(),
    KodeinAware,
    View.OnClickListener,
    DepositItemListener,
    CustomConfirmDialog.ConfirmationDialogListener{

    override val kodein by kodein()
    private val factory: DepositItemViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_deposit_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: DepositItemViewModel
        get() = ViewModelProvider(this, factory)[DepositItemViewModel::class.java]

    private var isReturnFlow = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.depositItemListener = this
        initView()
        initData()
    }

    private var returnItem: ItemReturn? = null

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnReopen?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnChangeLocker?.setOnClickListener(this)
    }

    private fun initData(){
        if (arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL) != null) {
            viewModel.typeInput.value = arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL)
            isReturnFlow = false
        }else viewModel.typeInput.value = ConstantUtils.TYPE_RETURN
        returnItem = arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
        returnItem?.let {
            viewModel.modelName.value = it.modelName
            viewModel.initialCheckStatus(it)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btnReopen -> {
                returnItem?.lockerId?.let { it1 -> viewModel.reopenLocker(it1) }
            }
            R.id.btnChangeLocker -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            R.id.btn_process -> {
                returnItem?.let {
                    viewModel.handleReturnItemProcess(it)
                }
            }
        }
    }

    override fun returnItemSuccess() {
        navigateTo(R.id.action_depositItemFragment_to_thankFragment, null)
    }

    override fun topupItemSuccess() {
        val dialog = CustomConfirmDialog.newInstance(
            message = getString(R.string.deposit_topup_success),
        )
        dialog.show(childFragmentManager, InputSerialNumberFragment.CONFIRMATION_DIALOG_TAG)
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        navigateTo(R.id.action_depositItemFragment2_to_adminDashboardFragment, null)
    }

    override fun onDialogCancelClick() {
        navigateTo(R.id.action_depositItemFragment2_to_adminDashboardFragment, null)
    }
}