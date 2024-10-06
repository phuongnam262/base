package com.lock.smartlocker.ui.scan_work_card

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentScanWorkCardBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.manager_menu.ManagerMenuActivity
import com.lock.smartlocker.ui.returns.ReturnActivity
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.Coroutines
import com.lock.smartlocker.util.SerialControlManager
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
        SerialControlManager.getSerialControl()?.onDataReceivedListener = { receivedData ->
            Coroutines.main {
                mViewDataBinding?.etWorkCard?.setText(receivedData)
            }
        }
        viewModel.scanCardListener = this
        (activity as? ReturnActivity)?.viewModel?.textEndScan?.value = ""
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scanCardListener = null
        (activity as? ManagerMenuActivity)?.viewModel?.textEndScan?.removeObservers(viewLifecycleOwner)
        (activity as? ReturnActivity)?.viewModel?.textEndScan?.removeObservers(viewLifecycleOwner)
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_work_card))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)
    }

    private fun initData(){
        if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
            viewModel.typeOpen = arguments?.getString(ConstantUtils.TYPE_OPEN)
        }
        (activity as? ManagerMenuActivity)?.viewModel?.textEndScan?.observe(viewLifecycleOwner) { text ->
            Log.d("textEndScan", text)
            mViewDataBinding?.etWorkCard?.setText(text)
        }
        (activity as? ReturnActivity)?.viewModel?.textEndScan?.observe(viewLifecycleOwner) { text ->
            Log.d("textEndScan", text)
            mViewDataBinding?.etWorkCard?.setText(text)
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.btn_process -> {
                    if(viewModel.typeOpen == ConstantUtils.TYPE_CONSUMABLE_COLLECT) viewModel.endUserLogin()
                    else viewModel.checkCardNumber()
                }
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> {
                    (activity as? ManagerMenuActivity)?.viewModel?.textEndScan?.value = ""
                    (activity as? ReturnActivity)?.viewModel?.textEndScan?.value = ""
                    viewModel.showStatusText.value = false
                    viewModel.workCardText.value = ""
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        }
    }

    override fun handleSuccess(name: String, cardNumber: String) {
        if (viewModel.typeOpen != ConstantUtils.TYPE_CONSUMABLE_COLLECT){
            // Nếu không phải consumable thì qua màn register face
            val bundle = Bundle().apply {
                putString(ConstantUtils.NAME_END_USER, name)
                putString(ConstantUtils.WORK_CARD_NUMBER, cardNumber)
            }
            navigateTo(R.id.action_scanWorkCardFragment2_to_registerFaceFragment, bundle)
        }else{
            navigateTo(R.id.action_scanWorkCardFragment_to_categoryConsumableCollectFragment, null)
        }
        viewModel.workCardText.postValue("")
    }

    override fun onDestroy() {
        super.onDestroy()
        SerialControlManager.getSerialControl()?.onDataReceivedListener = null
    }

    override fun handleFail() {
        mViewDataBinding?.etWorkCard?.text = null
        mViewDataBinding?.etWorkCard?.isEnabled = true
        mViewDataBinding?.etWorkCard?.requestFocus()
        mViewDataBinding?.etWorkCard?.showSoftInputOnFocus = false
    }
}
