package com.lock.smartlocker.ui.scan_work_card

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentScanWorkCardBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.ui.scan_item.adapter.ScanItem
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ScanWorkCardFragment : BaseFragment<FragmentScanWorkCardBinding, ScanWorkCardViewModel>(),
    KodeinAware, View.OnClickListener, ScanCardListener {

    override val kodein by kodein()
    private val factory: ScanWorkCardViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_scan_work_card
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: ScanWorkCardViewModel
        get() = ViewModelProvider(this, factory)[ScanWorkCardViewModel::class.java]
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.scanCardListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_work_card))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)
    }

    private fun initData(){
        viewModel.workCardText.observe(viewLifecycleOwner) { text ->
            viewModel.enableButtonProcess.postValue(text.isNotEmpty())
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.btn_process -> viewModel.checkCardNumber()
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    override fun handleSuccess(name: String, cardNumber: String) {
        val bundle = Bundle().apply {
            putString(ConstantUtils.NAME_END_USER, name)
            putString(ConstantUtils.WORK_CARD_NUMBER, cardNumber)
        }
        navigateTo(R.id.action_scanWorkCardFragment2_to_registerFaceFragment, bundle)
    }
}