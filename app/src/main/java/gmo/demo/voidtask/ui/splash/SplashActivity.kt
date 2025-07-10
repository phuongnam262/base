package gmo.demo.voidtask.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import gmo.demo.voidtask.databinding.ActivitySplashBinding
import gmo.demo.voidtask.ui.base.BaseActivity
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.preference.PreferenceHelper
import gmo.demo.voidtask.ui.login.LoginActivity
import gmo.demo.voidtask.ui.productlist.ProductListActivity
import gmo.demo.voidtask.util.CommonUtils
import gmo.demo.voidtask.util.ConstantUtils
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SplashActivity", "start")
        CommonUtils.runTimeDelay(ConstantUtils.SPLASH_TIME_OUT.toLong()) {
            val token = PreferenceHelper.getString(ConstantUtils.API_TOKEN, "")
            Log.d("SplashActivity", "Token: $token")
            Log.d("SplashActivity", "Token before check: $token")
            if (token.isNotEmpty()) {
                Log.d("SplashActivity", "Chuyển sang ProductListActivity")
                startActivity(Intent(this, ProductListActivity::class.java))
            } else {
                Log.d("SplashActivity", "Chuyển sang LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
            }
            Log.d("SplashActivity", "end")
            finish()
        }
    }
}