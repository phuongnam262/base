package com.lock.smartlocker.ui.openlocker

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivityOpenBinding
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.home.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.Timer
import kotlin.concurrent.timerTask

class OpenLockerActivity : BaseActivity<ActivityOpenBinding, OpenLockerViewModel>(),
    OpenLockerListener, KodeinAware {

    override val kodein by kodein()
    private val factory: OpenLockerViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_open
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: OpenLockerViewModel
        get() = ViewModelProvider(this, factory)[OpenLockerViewModel::class.java]

    private val timer = Timer()
    companion object {
        private const val TAG = "OpenActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.numberLocker= intent.getStringExtra(HomeActivity.NUMBER_AVAILABLE)?.toInt()
        viewModel.typeOPen = intent.getStringExtra(HomeActivity.TYPE_OPEN)
        viewModel.personCode = intent.getStringExtra(HomeActivity.PERSON_CODE).toString()
        viewModel.openLockerListener = this
        mViewDataBinding?.btnEnd?.setOnClickListener {
            finish()
            timer.cancel()
        }
        mViewDataBinding?.tvNumberLocker?.text = viewModel.numberLocker.toString()
        mViewDataBinding?.tvNumberOpen?.text = viewModel.numberLocker.toString()
        goSleepThread()
    }

    private fun goSleepThread() {
        timer.schedule(timerTask {
            finish()
        }, 5000)
    }

    override fun openSuccess() {

    }
}