package com.lock.smartlocker.ui.deposit_item

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.databinding.FragmentDepositItemBinding
import com.lock.smartlocker.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DepositItemFragment : BaseFragment<FragmentDepositItemBinding, DepositItemViewModel>(),
    KodeinAware,
    View.OnClickListener,
    DepositItemListener {

    companion object {
        const val RETURN_ITEM_REQUEST_KEY = "return_item_request"
    }

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

    private var returnItemRequest: ReturnItemRequest? = null // Khai báo ở đây

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnReopen?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnChangeLocker?.setOnClickListener(this)
    }

    private fun initData(){
        returnItemRequest = arguments?.getSerializable(RETURN_ITEM_REQUEST_KEY) as? ReturnItemRequest
        returnItemRequest?.let {
            it.serial_number?.let { it1 -> viewModel.getModelName(it1) }
            viewModel.initialCheckStatus(it)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btnReopen -> {
                returnItemRequest?.locker_id?.let { it1 -> viewModel.reopenLocker(it1) }
            }
            R.id.btnChangeLocker -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            R.id.btn_process -> {
                returnItemRequest?.let {
                    viewModel.handleReturnItemProcess(it)
                }
            }
        }
    }

    override fun returnItemSuccess() {
        navigateTo(R.id.action_depositItemFragment_to_thankFragment, null)
    }
}