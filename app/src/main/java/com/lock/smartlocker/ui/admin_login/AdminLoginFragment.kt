package com.lock.smartlocker.ui.admin_login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.databinding.FragmentAdminLoginBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.Coroutines
import kotlinx.coroutines.launch
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
        get() = ViewModelProvider(this, factory)[AdminLoginViewModel::class.java]
    private var isClicked = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel?.adminLoginListener = this
        initView()
        initData()
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
    }

    private fun initData() {

    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
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
        if (adminLoginResponse.staff.isAdminConsole) {
            if (adminLoginResponse.staff.isOtp) {
                // OTP true thì qua màn OTP
                val bundle = Bundle().apply {
                    putString(InputEmailFragment.EMAIL_REGISTER, adminLoginResponse.staff.email)
                    putString(
                        ConstantUtils.TYPE_OPEN_MANAGER,
                        arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER)
                    )
                }
                navigateTo(R.id.action_adminLoginFragment_to_inputOTPFragment, bundle)
            } else {
                // OTP false thì qua màn menu hoặc face list
                if(arguments?.getString(ConstantUtils.TYPE_OPEN_MANAGER) == ConstantUtils.TYPE_ADMIN_CONSOLE){
                    navigateTo(R.id.action_adminLoginFragment_to_adminDashboardFragment, null)
                }else{
                    navigateTo(R.id.action_adminLoginFragment_to_faceListFragment, null)
                }
            }
        }else{
            viewModel.mStatusText.postValue(R.string.error_no_permission_console)
        }
    }
}