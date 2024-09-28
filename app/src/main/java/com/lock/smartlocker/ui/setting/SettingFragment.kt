package com.lock.smartlocker.ui.setting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.databinding.FragmentSettingBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.util.ConstantUtils
import com.lock.smartlocker.util.KioskModeHelper
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(),
    KodeinAware, View.OnClickListener {

    override val kodein by kodein()
    private val factory: SettingViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_setting
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: SettingViewModel
        get() = ViewModelProvider(this, factory)[SettingViewModel::class.java]
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.setting))
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        mViewDataBinding?.swNavigation?.isChecked = true
        mViewDataBinding?.swStatus?.isChecked = true
        activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_NAVIGATION_BAR) }
        activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_STATUS_BAR) }
        mViewDataBinding?.swNavigation?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_NAVIGATION_BAR) }
            }else{
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.HIDE_NAVIGATION_BAR) }
            }
        }
        mViewDataBinding?.swStatus?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.SHOW_STATUS_BAR) }
            }else {
                activity?.let { KioskModeHelper.sendCommand(it, KioskModeHelper.Action.HIDE_STATUS_BAR) }
            }
        }
        mViewDataBinding?.swLight?.isChecked =
            PreferenceHelper.getBoolean(ConstantUtils.LIGHT_ON, false)

        mViewDataBinding?.swLight?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                PreferenceHelper.writeBoolean(ConstantUtils.LIGHT_ON, true)
            }else {
                PreferenceHelper.writeBoolean(ConstantUtils.LIGHT_ON, false)
            }
        }
    }

    private fun initData(){
        viewModel.getInformationStaff()
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }
}