package com.lock.smartlocker.ui.inputotp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentInputOtpBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class InputOTPFragment : BaseFragment<FragmentInputOtpBinding, InputOTPViewModel>(), KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: InputOTPViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_input_otp
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: InputOTPViewModel
        get() = ViewModelProvider(this, factory)[InputOTPViewModel::class.java]
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.auth_required))
        mViewDataBinding?.btnResendOtp?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
    }

    private fun initData(){

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_resend_otp -> navigateTo(R.id.action_inputOTPFragment_to_registerFaceFragment, null)
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {
                if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
                    val bundle = Bundle().apply {
                        putString(ConstantUtils.TYPE_OPEN, arguments?.getString(ConstantUtils.TYPE_OPEN))
                    }
                    navigateTo(R.id.action_inputOTPFragment2_to_categoryFragment, bundle)
                }else{
                    val isOpenManager = arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER) != null
                    val bundle = Bundle().apply {
                        putString(InputEmailFragment.EMAIL_REGISTER, arguments?.getString(
                            InputEmailFragment.EMAIL_REGISTER))
                    }
                    if(isOpenManager){
                        if (arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER) == ConstantUtils.TYPE_ADMIN_CONSOLE) {
                            navigateTo(
                                R.id.action_inputOTPFragment_to_adminDashboardFragment,
                                bundle
                            )
                        } else {
                            navigateTo(
                                R.id.action_inputOTPFragment_to_faceListFragment,
                                bundle
                            )
                        }
                    }else{
                        navigateTo(R.id.action_inputOTPFragment_to_registerFaceFragment, bundle)
                    }
                }
            }
        }
    }
}