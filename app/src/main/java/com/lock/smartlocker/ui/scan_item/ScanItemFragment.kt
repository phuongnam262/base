package com.lock.smartlocker.ui.scan_item

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.LockerInfo
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentScanItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.scan_item.adapter.ScanItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ScanItemFragment : BaseFragment<FragmentScanItemBinding, ScanItemViewModel>(),
    KodeinAware,
    View.OnClickListener, ScanItemListener {

    override val kodein by kodein()
    private val factory: ScanItemViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_scan_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val scanItemAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: ScanItemViewModel
        get() = ViewModelProvider(this, factory)[ScanItemViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scanItemListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scanItemListener = null
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_items))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)
        mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE
    }

    private fun initData(){
        arguments?.let {
            val jsonList = PreferenceHelper.getString(ConstantUtils.LOCKER_INFOS, "")
            val responseType = object : TypeToken<ArrayList<LockerInfo>>() {}.type
            val listLockerInfo: ArrayList<LockerInfo> = Gson().fromJson(jsonList, responseType)
            viewModel.listLockerInfo.value = listLockerInfo
            viewModel.transactionId.value = it.getString(ConstantUtils.TRANSACTION_ID)
        }
        viewModel.listLockerInfo.observe(viewLifecycleOwner) { collect ->
            scanItemAdapter.update(collect.map {
                activity?.let { it1 -> ScanItem(it1, it, viewModel) }
            })
            viewModel.listLockerId.value = collect.map { it.lockerId }
        }
        mViewDataBinding?.rvLockers?.adapter = scanItemAdapter
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.btn_process -> {
                    viewModel.updateInventoryTransaction()
                }
            }
        }
    }

    override fun updateInventorySuccess() {
        navigateTo(R.id.action_scanItemFragment_to_thankFragment2, null)
    }
}