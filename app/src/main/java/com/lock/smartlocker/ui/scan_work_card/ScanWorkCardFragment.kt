package com.lock.smartlocker.ui.scan_work_card

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentScanWorkCardBinding
import com.lock.smartlocker.ui.base.BaseFragment
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
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.scanCardListener = null
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.scan_work_card))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)

        mViewDataBinding?.etWorkCard?.requestFocus()
        mViewDataBinding?.etWorkCard?.showSoftInputOnFocus = false
        val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(mViewDataBinding?.etWorkCard?.windowToken, 0)
        mViewDataBinding?.etWorkCard?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputMethodManager.hideSoftInputFromWindow(mViewDataBinding?.etWorkCard?.windowToken, 0)
                mViewDataBinding?.etWorkCard?.isEnabled = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                inputMethodManager.hideSoftInputFromWindow(mViewDataBinding?.etWorkCard?.windowToken, 0)
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.showStatusText.value = false
                inputMethodManager.hideSoftInputFromWindow(mViewDataBinding?.etWorkCard?.windowToken, 0)
            }
        })
    }

    private fun initData(){
        if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
            viewModel.typeOpen = arguments?.getString(ConstantUtils.TYPE_OPEN)
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
