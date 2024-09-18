package com.lock.smartlocker.ui.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivitySplashBinding
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.home.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(), SplashListener,
    KodeinAware {

    override val kodein by kodein()
    private val factory: SplashViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_splash
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: SplashViewModel
        get() = ViewModelProvider(this, factory)[SplashViewModel::class.java]

    companion object {
        private const val TAG = "SplashActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding
        viewModel.splashListener = this
        viewModel.terminalLogin()
        viewModel.allSuccess.observe(this){
            startActivity(HomeActivity::class.java)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun updateLayout() {
        ///Xử lý j ở đây
    }
}