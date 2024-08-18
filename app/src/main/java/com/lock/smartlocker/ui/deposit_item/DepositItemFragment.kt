package com.lock.smartlocker.ui.deposit_item

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.databinding.FragmentDepositItemBinding
import com.lock.smartlocker.databinding.FragmentInputSerialNumberBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.home.HomeActivity
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberListener
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModel
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberViewModelFactory
import com.lock.smartlocker.ui.thanks.ThankActivity
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class DepositItemFragment : BaseFragment<FragmentDepositItemBinding, DepositItemViewModel>(),
    KodeinAware,
    View.OnClickListener,
    DepositItemListener {

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

    private fun initView(){
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnReopen?.setOnClickListener(this)
        mViewDataBinding?.depositItem?.btnChangeLocker?.setOnClickListener(this)

    }

    private fun initData(){
        viewModel.checkStatus()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btnReopen -> {}
            R.id.btnChangeLocker -> {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            R.id.btn_process -> {
                viewModel.checkStatus()
                viewModel.doorStatus.observe(viewLifecycleOwner) { doorStatus ->
                    if (doorStatus == 0) {
                        viewModel.handleError(ConstantUtils.DOOR_HAS_NOT_BEEN_CLOSE)
                    } else {
                        val returnItemRequest = arguments?.getSerializable("return_item_request") as? ReturnItemRequest
                        if (returnItemRequest != null) {
                            viewModel.returnItem(returnItemRequest)
                        }
                    }
                    viewModel.doorStatus.removeObservers(viewLifecycleOwner)
                }
            }
        }
    }

    override fun returnItemSuccess() {
        val intent = Intent(requireContext(), ThankActivity::class.java)
        requireActivity().startActivity(intent)
    }
}