package com.lock.basesource.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lock.basesource.databinding.ActivitySplashBinding
import com.lock.basesource.ui.base.BaseActivity
import com.lock.basesource.BR
import com.lock.basesource.R
import com.lock.basesource.ui.home.HomeActivity
import com.lock.basesource.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(),
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
        runTimeDelay(ConstantUtils.SPLASH_TIME_OUT.toLong()) {
            startActivity(HomeActivity::class.java)
            finish()
        }
    }
}