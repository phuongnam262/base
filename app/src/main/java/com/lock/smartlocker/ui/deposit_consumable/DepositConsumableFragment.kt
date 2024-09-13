package com.lock.smartlocker.ui.deposit_consumable

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerConsumable
import com.lock.smartlocker.databinding.FragmentDepositConsumableBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.deposit_consumable.adapter.ConsumableTopupItem
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DepositConsumableFragment : BaseFragment<FragmentDepositConsumableBinding, DepositConsumableViewModel>(),
    KodeinAware,
    View.OnClickListener,
    DepositConsumableListener,
    CustomConfirmDialog.ConfirmationDialogListener{

    override val kodein by kodein()
    private val factory: DepositConsumableViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_deposit_consumable
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: DepositConsumableViewModel
        get() = ViewModelProvider(this, factory)[DepositConsumableViewModel::class.java]

    private var lockerId: String? = null
    private val consumableAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.depositConsumableListener = this
        initView()
        initData()
    }

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setText(R.string.confirm_button)
        mViewDataBinding?.btnReopen?.setOnClickListener(this)
        mViewDataBinding?.btnLocker?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.rvConsumables?.adapter = consumableAdapter
    }

    private fun initData(){
        lockerId = arguments?.getString(ConstantUtils.LOCKER_ID)
        lockerId?.let { viewModel.checkLockerStatus(it) }
        viewModel.lockerConsumable.observe(viewLifecycleOwner) { locker ->
            mViewDataBinding?.locker = locker
        }
        viewModel.listConsumable.observe(viewLifecycleOwner) { models ->
            consumableAdapter.update(models.map { ConsumableTopupItem(it, viewModel) })
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.btnReopen, R.id.btnLocker -> {
                    lockerId?.let {
                        it1 -> viewModel.reopenLocker(it1)
                    }
                }

                R.id.btn_process -> {
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

    override fun sendCommandOpenLockerSuccess(lockerId: String?) {
        TODO("Not yet implemented")
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        navigateTo(R.id.action_depositItemFragment2_to_adminDashboardFragment, null)
    }

    override fun onDialogCancelClick() {
        navigateTo(R.id.action_depositItemFragment2_to_adminDashboardFragment, null)
    }
}