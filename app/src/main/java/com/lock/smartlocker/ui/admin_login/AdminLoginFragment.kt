package com.lock.smartlocker.ui.admin_login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentAdminLoginBinding
import com.lock.smartlocker.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AdminLoginFragment : BaseFragment<FragmentAdminLoginBinding, AdminLoginViewModel>(),
    KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: AdminLoginViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_admin_login
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: AdminLoginViewModel
        get() = ViewModelProvider(this, factory)[AdminLoginViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        viewModel.titlePage.postValue(getString(R.string.auth_required))
        viewModel.startTimer()
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
    }

    private fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_home -> activity?.finish()
            R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            R.id.btn_process -> {

            }
        }
    }
}