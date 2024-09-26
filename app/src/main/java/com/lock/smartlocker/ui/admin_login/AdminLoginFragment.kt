package com.lock.smartlocker.ui.admin_login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.databinding.FragmentAdminLoginBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AdminLoginFragment : BaseFragment<FragmentAdminLoginBinding, AdminLoginViewModel>(),
    KodeinAware, View.OnClickListener, AdminLoginListener {

    override val kodein by kodein()
    private val factory: AdminLoginViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_admin_login
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: AdminLoginViewModel
        get() = ViewModelProvider(requireActivity(), factory)[AdminLoginViewModel::class.java]
    private var isClicked = false
    private var typeOpen : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel?.adminLoginListener = this
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.adminLoginListener = null
    }

    override fun onResume() {
        super.onResume()
        isClicked = false
    }

    private fun initView() {
        viewModel.titlePage.postValue(getString(R.string.auth_required))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)
    }

    private fun initData() {
        if (arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER) != null) {
            typeOpen = arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER)
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.finish()
                R.id.btn_process -> {
                    if (isClicked.not()) {
                        isClicked = true
                        viewModel.adminLogin()
                    }
                }
            }
        }
    }

    override fun adminLoginSuccess(adminLoginResponse: AdminLoginResponse) {
        //Đăng nhập thành công -> check permission is_admin_console = false
        viewModel.username.postValue("")
        viewModel.password.postValue("")
        if (adminLoginResponse.staff.isAdminConsole) {
            if (adminLoginResponse.staff.isOtp) {
                // OTP true thì qua màn OTP
                val bundle = Bundle().apply {
                    putString(InputEmailFragment.EMAIL_REGISTER, adminLoginResponse.staff.email)
                    putString(
                        ConstantUtils.TYPE_OPEN_MANAGER, typeOpen)
                }
                navigateTo(R.id.action_adminLoginFragment_to_inputOTPFragment, bundle)
            } else {
                // OTP false thì qua màn menu hoặc face list
                if(typeOpen == ConstantUtils.TYPE_ADMIN_CONSOLE){
                    navigateTo(R.id.action_adminLoginFragment_to_adminDashboardFragment, null)
                }else{
                    navigateTo(R.id.action_adminLoginFragment_to_faceListFragment, null)
                }
            }
        }else{
            viewModel.mStatusText.postValue(R.string.error_no_permission_console)
        }
    }

    override fun adminLoginFail() {
        isClicked = false
    }
}