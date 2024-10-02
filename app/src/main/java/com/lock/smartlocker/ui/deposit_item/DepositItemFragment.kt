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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.depositItemListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.depositItemListener = null
        viewModel.doorStatus.removeObservers(viewLifecycleOwner)
        viewModel.stopChecking()
    }

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.confirm_button)
        mViewDataBinding?.depositItem?.btnReopen?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnChangeLocker?.setOnClickListener(this)
        if (viewModel.isRequireCloseDoor) viewModel.enableButtonProcess.value = false
    }

    private fun initData(){
        viewModel.isReturnFlow =
            arguments?.getString(InputSerialNumberFragment.TYPE_INPUT_SERIAL) == null
        viewModel.returnItem = arguments?.getSerializable(InputSerialNumberFragment.RETURN_ITEM_REQUEST_KEY) as? ItemReturn
        viewModel.returnItem?.let {
            viewModel.modelName.value = it.modelName
            viewModel.lockerName.value = it.lockerName
            viewModel.arrowPosition.value = it.arrowPosition
            viewModel.doorStatus.value = it.doorStatus
        }

        viewModel.doorStatus.observe(viewLifecycleOwner) {
            if (viewModel.isRequireCloseDoor.not()) viewModel.enableButtonProcess.value = it == 0
            if (it == 0) {
                viewModel.showStatusText.value = false
                if (viewModel.isRequireCloseDoor) viewModel.startCheckingStatus()
                else viewModel.mStatusText.postValue(R.string.door_has_not_been_closed)
            }else {
                viewModel.mStatusText.postValue(R.string.error_open_failed)
            }
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.supportFragmentManager?.popBackStack()
                R.id.btnReopen -> {
                    viewModel.returnItem?.lockerId?.let { it1 -> viewModel.reopenLocker(it1) }
                }
                R.id.btnChangeLocker -> {
                    activity?.supportFragmentManager?.popBackStack()
                }
                R.id.btn_process -> {
                    if (viewModel.isRequireCloseDoor.not() || viewModel.autoCheckDoorStatus.value == 1) {
                        viewModel.returnItem?.let {
                            viewModel.showStatusText.value = false
                            viewModel.handleReturnItemProcess(it)
                        }
                    } else {
                        viewModel.checkStatusCloseOrNot(viewModel.returnItem?.lockerId ?: "")
                    }
                }
            }
        }
    }

    override fun returnItemSuccess() {
        showDialogConfirm(getString(R.string.return_topup_success), "return")
    }

    override fun topupItemSuccess() {
        showDialogConfirm(getString(R.string.deposit_topup_success), "topup")
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        val bundle = Bundle().apply {
            putString(
                InputSerialNumberFragment.TYPE_INPUT_SERIAL,
                ConstantUtils.TYPE_TOPUP_ITEM
            )
        }
        if (dialogTag.equals("return")) navigateTo(R.id.action_depositItemFragment_to_inputSerialNumberFragment, null)
        else navigateTo(R.id.action_depositItemFragment2_to_inputSerialNumberFragment2, bundle)
    }

    override fun onDialogCancelClick(dialogTag: String?) {
        if (dialogTag.equals("return")) navigateTo(R.id.action_depositItemFragment_to_thankFragment, null)
        else navigateTo(R.id.action_depositItemFragment2_to_adminDashboardFragment, null)
    }
}