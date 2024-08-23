package com.lock.smartlocker.ui.collect_items

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.responses.CreateInventoryResponse
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentCollectItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.collect_items.adapter.CollectItem
import com.lock.smartlocker.util.ConstantUtils
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class CollectItemFragment : BaseFragment<FragmentCollectItemBinding, CollectItemViewModel>(),
    KodeinAware,
    View.OnClickListener, CollectItemListener {

    override val kodein by kodein()
    private val factory: CollectItemViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_collect_item
    override val bindingVariable: Int
        get() = BR.viewmodel

    private val collectItemAdapter = GroupAdapter<GroupieViewHolder>()

    override val viewModel: CollectItemViewModel
        get() = ViewModelProvider(this, factory)[CollectItemViewModel::class.java]

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
        arguments?.let {
            val jsonList = PreferenceHelper.getString(ConstantUtils.LOCKER_INFOS, "")
            val responseType = object : TypeToken<CreateInventoryResponse>() {}.type
            val listLockerInfo: CreateInventoryResponse = Gson().fromJson(jsonList, responseType)
            viewModel.listLockerInfo.value = listLockerInfo.locker_infos
            viewModel.transactionId.value = it.getString(ConstantUtils.TRANSACTION_ID)
        }
        viewModel.listLockerInfo.observe(viewLifecycleOwner) { collect ->
            collectItemAdapter.update(collect?.map {
                CollectItem(it, viewModel)
            } ?: emptyList())
            viewModel.listLockerId.value = collect?.map { it.lockerId }
        }
        viewModel.listLockerId.observe(viewLifecycleOwner) {
            viewModel.openLocker()
        }
        mViewDataBinding?.rvLockers?.adapter = collectItemAdapter
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.btn_process -> {
                viewModel.updateInventoryTransaction()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun sendCommandOpenLockerSuccess() {
    }

    override fun updateInventorySuccess() {
        val bundle = Bundle().apply {
            putString(ConstantUtils.TRANSACTION_ID, viewModel.transactionId.value)
            //putParcelableArrayList(ConstantUtils.LOCKER_INFOS,ArrayList(listLockerInfo))
        }
        navigateTo(R.id.action_collectItemFragment_to_scanItemFragment, bundle,)
    }
}