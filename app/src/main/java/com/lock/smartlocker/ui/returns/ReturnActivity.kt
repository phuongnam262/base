package com.lock.smartlocker.ui.returns

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.data.models.CartItem
import com.lock.smartlocker.databinding.ActivityReturnBinding
import com.lock.smartlocker.ui.admin_login.AdminLoginFragment
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.input_serial_number.InputSerialNumberFragment
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.ui.recognize_face.RecognizeFaceFragment
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ReturnActivity : BaseActivity<ActivityReturnBinding, ReturnViewModel>(),
    ReturnListener,
    KodeinAware {

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

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            initView()
        }, 50)
    }

    private fun initView() {
        when (intent.getStringExtra(ConstantUtils.TYPE_OPEN)) {
            ConstantUtils.TYPE_LOAN -> {
                showFragment(RecognizeFaceFragment(), R.navigation.loan_collect_navigation, typeOpen = ConstantUtils.TYPE_LOAN)
            }

            ConstantUtils.TYPE_RETURN -> {
                showFragment(InputSerialNumberFragment(), R.navigation.return_navigation)
            }

            ConstantUtils.TYPE_COLLECT -> {
                showFragment(RecognizeFaceFragment(), R.navigation.loan_collect_navigation, typeOpen = ConstantUtils.TYPE_COLLECT)
            }
            ConstantUtils.TYPE_CONSUMABLE_COLLECT -> {
                showFragment(RecognizeFaceFragment(), R.navigation.loan_collect_navigation, typeOpen = ConstantUtils.TYPE_CONSUMABLE_COLLECT)
            }
        }
    }
}