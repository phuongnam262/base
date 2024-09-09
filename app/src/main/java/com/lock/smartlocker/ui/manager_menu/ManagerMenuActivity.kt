package com.lock.smartlocker.ui.manager_menu

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivityManagerMenuBinding
import com.lock.smartlocker.ui.admin_login.AdminLoginFragment
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.inputemail.InputEmailFragment
import com.lock.smartlocker.ui.menu_register.MenuRegisterFragment
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ManagerMenuActivity : BaseActivity<ActivityManagerMenuBinding, ManagerMenuViewModel>(),
    ManagerMenuListener,
    KodeinAware {

    override val kodein by kodein()
    private val factory: ManagerMenuViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_manager_menu
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: ManagerMenuViewModel
        get() = ViewModelProvider(this, factory)[ManagerMenuViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding
        viewModel.managerMenuListener = this
        initView()
    }

    private fun initView() {
        when (intent.getStringExtra(ConstantUtils.TYPE_OPEN_MANAGER)) {
            ConstantUtils.TYPE_REGISTER_FACE -> {
                showFragment(MenuRegisterFragment(), R.navigation.register_face_navigation)
            }

            ConstantUtils.TYPE_MANAGER_FACE -> {
                showFragment(AdminLoginFragment(), R.navigation.admin_console_navigation, typeManager = ConstantUtils.TYPE_MANAGER_FACE)
            }

            ConstantUtils.TYPE_ADMIN_CONSOLE -> {
                showFragment(AdminLoginFragment(), R.navigation.admin_console_navigation, typeManager = ConstantUtils.TYPE_ADMIN_CONSOLE)
            }
        }
    }
}