package com.mbf.voidtask.ui.inputotp

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mbf.voidtask.BR
import com.mbf.voidtask.R
import com.mbf.voidtask.databinding.FragmentInputOtpBinding
import com.mbf.voidtask.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class InputOTPFragment : BaseFragment<FragmentInputOtpBinding, InputOTPViewModel>(), KodeinAware,
    View.OnClickListener, InputOTPListener {

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

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun initView(){
    }

    private fun initData(){
    }

    override fun onClick(v: View?) {

    }

    override fun verifySuccess(email: String?) {
    }

    override fun verifyFail() {
    }
}