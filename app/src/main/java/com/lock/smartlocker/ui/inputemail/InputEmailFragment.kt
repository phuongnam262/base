package com.lock.smartlocker.ui.inputemail

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentInputEmailBinding
import com.lock.smartlocker.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class InputEmailFragment : BaseFragment<FragmentInputEmailBinding, InputEmailViewModel>(), KodeinAware,
    View.OnClickListener, InputEmailListener {

    override val kodein by kodein()
    private val factory: InputEmailViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_input_email
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: InputEmailViewModel
        get() = ViewModelProvider(this, factory)[InputEmailViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.inputEmailListener = this
        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.auth_required))
        viewModel.spinnerItems.observe(viewLifecycleOwner) { items ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                items
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mViewDataBinding?.spiEmail?.adapter = adapter
            viewModel.subEmail.value = items[0]
        }
        viewModel.loadSpinnerItems()
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
    }

    private fun initData(){
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {
                viewModel.consumerLogin()
            }
        }
    }

    override fun consumerLoginSuccess() {
        navigateTo(R.id.action_navigation_input_email_to_inputOTPFragment, null)
    }
}