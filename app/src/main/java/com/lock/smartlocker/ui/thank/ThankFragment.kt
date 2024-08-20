package com.lock.smartlocker.ui.thank

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentThankBinding
import com.lock.smartlocker.ui.base.BaseFragment
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ThankFragment : BaseFragment<FragmentThankBinding, ThankViewModel>(),
    KodeinAware,
    View.OnClickListener{

    override val kodein by kodein()
    private val factory: ThankViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_thank
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: ThankViewModel
        get() = ViewModelProvider(this, factory)[ThankViewModel::class.java]


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        viewModel.startTimer()
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
    }

    private fun initData(){
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_home -> activity?.finish()
        }
    }

}