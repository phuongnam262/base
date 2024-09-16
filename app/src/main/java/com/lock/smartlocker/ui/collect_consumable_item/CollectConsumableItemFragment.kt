package com.lock.smartlocker.ui.collect_consumable_item

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.responses.CreateTransactionResponse
import com.lock.smartlocker.data.models.LockerInfoCollect
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentCollectConsumableItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.collect_consumable_item.adapter.CollectItem
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CollectConsumableItemFragment : BaseFragment<FragmentCollectConsumableItemBinding, CollectConsumableItemViewModel>(),
    KodeinAware,
    View.OnClickListener, CollectConsumableItemListener,
    CustomConfirmDialog.ConfirmationDialogListener{

    override val kodein by kodein()
    private val factory: CollectConsumableItemViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_collect_consumable_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val collectItemAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CollectConsumableItemViewModel
        get() = ViewModelProvider(this, factory)[CollectConsumableItemViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.collectItemListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.collect_items))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE
    }

    private fun initData(){
        arguments?.let { it ->
            val jsonList = PreferenceHelper.getString(ConstantUtils.LOCKER_INFOS, "")
            val responseType = object : TypeToken<CreateTransactionResponse>() {}.type
            val listLockerInfo: CreateTransactionResponse = Gson().fromJson(jsonList, responseType)
            val listLockerInfoCollect = listLockerInfo.locker_infos.flatMap { lockerInfo ->
                lockerInfo.consumableCollects.map { consumable ->
                    LockerInfoCollect(
                        lockerId = lockerInfo.lockerId,
                        lockerName = lockerInfo.lockerName,
                        arrowPosition = lockerInfo.arrowPosition,
                        doorStatus = 2,
                        consumableName = consumable.consumableName,
                        consumableId = consumable.consumableId,
                        categoryId = consumable.categoryId,
                        categoryName = consumable.categoryName,
                        currentQuantity = consumable.currentQuantity,
                        setPoint = consumable.setPoint,
                        takeNumber = consumable.takeNumber
                    )
                }
            }
            if (listLockerInfoCollect.isNotEmpty()) viewModel.enableButtonProcess.value = true
            viewModel.listLockerInfo.value = listLockerInfoCollect
            viewModel.transactionId.value = it.getString(ConstantUtils.TRANSACTION_ID)
        }
        viewModel.listLockerInfo.observe(viewLifecycleOwner) { collect ->
            collectItemAdapter.update(collect?.map {
                activity?.let { it1 -> CollectItem(it1,it, viewModel, childFragmentManager) }
            } ?: emptyList())
            viewModel.listLockerId.value = collect?.map { it.lockerId }
        }
        viewModel.listLockerId.observe(viewLifecycleOwner) {
            viewModel.openLocker()
        }
        mViewDataBinding?.rvLockers?.adapter = collectItemAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> showDialogConfirm(getString(R.string.dialog_cancel_process))
                R.id.btn_process -> {
                    if (viewModel.isConfirm.value == true) {
                        viewModel.confirmCollectConsumable()
                    } else {
                        viewModel.isConfirm.value = true
                        viewModel.titlePage.postValue(getString(R.string.confirmation))
                        mViewDataBinding?.bottomMenu?.btnProcess?.text = getString(R.string.confirm_button)
                        collectItemAdapter.notifyDataSetChanged()
                        viewModel.showStatusText.value = false
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun sendCommandOpenLockerSuccess() {
        collectItemAdapter.notifyDataSetChanged()
    }

    override fun confirmCollectSuccess() {
        navigateTo(R.id.action_collectConsumableItemFragment_to_thankFragment2, null)
    }

    override fun onDialogConfirmClick(dialogTag: String?) {
        activity?.finish()
    }

    override fun onDialogCancelClick() {
    }
}