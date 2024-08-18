package com.lock.smartlocker.ui.returns

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivityReturnBinding
import com.lock.smartlocker.ui.base.BaseActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ReturnActivity : BaseActivity<ActivityReturnBinding, ReturnViewModel>(),
    ReturnListener,
    KodeinAware, OnClickListener {

    override val kodein by kodein()
    private val factory: ReturnViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_return
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: ReturnViewModel
        get() = ViewModelProvider(this, factory)[ReturnViewModel::class.java]


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding
        viewModel.returnListener = this
        initView()
    }

    private fun initView() {
//        showFragment(InputSerialNumberFragment(), R.navigation.return_navigation)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
            }
        }
    }
}