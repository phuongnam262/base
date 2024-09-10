package com.lock.smartlocker.ui.admin_dashboard

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentAdminDashboardBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class AdminDashboardFragment : BaseFragment<FragmentAdminDashboardBinding, AdminDashboardViewModel>(),
    KodeinAware, View.OnClickListener, CustomConfirmDialog.ConfirmationDialogListener {

    override val kodein by kodein()
    private val factory: AdminDashboardViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_admin_dashboard
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: AdminDashboardViewModel
        get() = ViewModelProvider(this, factory)[AdminDashboardViewModel::class.java]
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.admin_menu))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.llConsumableTopup?.setOnClickListener(this)
        mViewDataBinding?.llTopupItems?.setOnClickListener(this)
        mViewDataBinding?.llCloseApp?.setOnClickListener(this)
        mViewDataBinding?.llSettings?.setOnClickListener(this)
        mViewDataBinding?.llManageLockers?.setOnClickListener(this)
        mViewDataBinding?.llRetrieveItems?.setOnClickListener(this)
        mViewDataBinding?.llRetrieveFaulty?.setOnClickListener(this)
    }

    private fun initData(){
        viewModel.getInformationStaff()
        viewModel.numberLockerAvailable.observe(viewLifecycleOwner) {
            mViewDataBinding?.tvTopupItems?.text = getString(R.string.topup_items, it.toString())
        }
        viewModel.numberItemFaulty.observe(viewLifecycleOwner) {
            mViewDataBinding?.tvRetrieveFaulty?.text = getString(R.string.retrieve_faulty, it.toString())
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.ll_topup_items -> {
                    if (viewModel.numberLockerAvailable.value != 0) {
                        val bundle = Bundle().apply {
                            putString(
                                InputSerialNumberFragment.TYPE_INPUT_SERIAL,
                                ConstantUtils.TYPE_TOPUP_ITEM
                            )
                        }
                        navigateTo(
                            R.id.action_adminDashboardFragment_to_inputSerialNumberFragment2,
                            bundle
                        )
                    } else viewModel.mMessage.postValue(R.string.error_no_available_locker)
                }

                R.id.ll_close_app -> {
                    val dialog = CustomConfirmDialog.newInstance(
                        message = getString(R.string.dialog_close_app),
                    )
                    dialog.show(
                        childFragmentManager,
                        InputSerialNumberFragment.CONFIRMATION_DIALOG_TAG
                    )

                }

                R.id.ll_settings -> {
                    navigateTo(R.id.action_adminDashboardFragment_to_settingFragment, null)
                }

                R.id.ll_manage_lockers -> {
                    navigateTo(R.id.action_adminDashboardFragment_to_manageLockerFragment, null)
                }

                R.id.ll_retrieve_items -> {
                    navigateTo(R.id.action_adminDashboardFragment_to_retrieveItemFragment, null)
                }

                R.id.ll_retrieve_faulty -> {
                    navigateTo(R.id.action_adminDashboardFragment_to_retrieveFaultyFragment, null)
                }
            }
        }
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        activity?.finishAffinity()
    }

    override fun onDialogCancelClick() {
    }
}