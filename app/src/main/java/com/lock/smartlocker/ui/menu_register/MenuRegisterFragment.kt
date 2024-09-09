package com.lock.smartlocker.ui.menu_register

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentMenuRegisterBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.view.custom.CustomConfirmDialog
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class MenuRegisterFragment : BaseFragment<FragmentMenuRegisterBinding, MenuRegisterViewModel>(),
    KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: MenuRegisterViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_menu_register
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: MenuRegisterViewModel
        get() = ViewModelProvider(this, factory)[MenuRegisterViewModel::class.java]
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.admin_menu))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.visibility = View.GONE
        mViewDataBinding?.llEmail?.setOnClickListener(this)
        mViewDataBinding?.llWorkCard?.setOnClickListener(this)
    }

    private fun initData(){

    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            val bundle = Bundle().apply {
                putString(ConstantUtils.TYPE_OPEN, arguments?.getString(ConstantUtils.TYPE_OPEN) )
            }
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.ll_email -> {
                    navigateTo(R.id.action_menuRegisterFragment_to_inputEmailFragment, bundle)
                }

                R.id.ll_work_card -> {
                    navigateTo(R.id.action_menuRegisterFragment_to_scanWorkCardFragment2, bundle)
                }
            }
        }
    }
}