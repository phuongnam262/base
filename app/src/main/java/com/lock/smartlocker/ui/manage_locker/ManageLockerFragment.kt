package com.lock.smartlocker.ui.manage_locker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentManageLockerBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.manage_locker.adapter.LockerItem
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ManageLockerFragment : BaseFragment<FragmentManageLockerBinding, ManageLockerViewModel>(),
    KodeinAware, View.OnClickListener, ManageLockerListener, CustomConfirmDialog.ConfirmationDialogListener {

    override val kodein by kodein()
    private val factory: ManageLockerViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_manage_locker
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: ManageLockerViewModel
        get() = ViewModelProvider(this, factory)[ManageLockerViewModel::class.java]

    private val lockerAdapter = GroupAdapter<GroupieViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.manageLockerListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.manage_locker))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnUsingMail?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnUsingMail?.text = getString(R.string.open_all_button)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.check_door_button)
        mViewDataBinding?.rvLockers?.adapter = lockerAdapter
    }

    private fun initData(){
        viewModel.getListLocker()
        viewModel.lockers.observe(viewLifecycleOwner) { lockers ->
            viewModel.listLockerId.value = lockers?.map { it.lockerId } ?: emptyList()
            lockerAdapter.update(lockers?.map { activity?.let { it1 ->
                LockerItem(
                    it1, it, viewModel, childFragmentManager)
            } } ?: emptyList())
        }
        viewModel.listLockerId.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                viewModel.openAllLocker()
            }
        }
        }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_using_mail ->{
                viewModel.openAllLocker()
            }
            R.id.btn_process ->{
                viewModel.checkDoorStatus()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun openLockerSuccess() {
       lockerAdapter.notifyDataSetChanged()
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        viewModel.disableLocker()
    }

    override fun onDialogCancelClick() {
    }
}