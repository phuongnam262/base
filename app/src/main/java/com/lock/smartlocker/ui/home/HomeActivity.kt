package com.lock.smartlocker.ui.home

import android.Manifest
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivityHomeBinding
import com.lock.smartlocker.ui.base.BaseActivity
import com.lock.smartlocker.ui.manager_menu.ManagerMenuActivity
import com.lock.smartlocker.ui.returns.ReturnActivity
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(), HomeListener,
    KodeinAware, OnClickListener {

    override val kodein by kodein()
    private val factory: HomeViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_home
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: HomeViewModel
        get() = ViewModelProvider(this, factory)[HomeViewModel::class.java]

    companion object {
        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUESTS = 1

        private val REQUIRED_RUNTIME_PERMISSIONS =
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        const val NUMBER_AVAILABLE = "number_available"
        const val TYPE_OPEN = "type_open"
        const val PERSON_CODE = "person_code"
    }

    private var isOpenLocalServer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Data binding
        viewModel.homeListener = this
        viewModel.startTimer()
        if (!allRuntimePermissionsGranted()) {
            getRuntimePermissions()
        }
        mViewDataBinding?.btnMenu?.setOnClickListener(this)
        mViewDataBinding?.navMenuLeft?.llRegisterFace?.setOnClickListener(this)
        mViewDataBinding?.navMenuLeft?.llManageFace?.setOnClickListener(this)
        mViewDataBinding?.navMenuLeft?.llAdminConsole?.setOnClickListener(this)
        mViewDataBinding?.tvEn?.setOnClickListener(this)
        mViewDataBinding?.tvVi?.setOnClickListener(this)
        viewModel.checkOpenServer()
        viewModel.isServerOff.observeForever {
            isOpenLocalServer = it
            if (it) {
                viewModel.addGroup()
            } else {
                Toast.makeText(this, "Please Open Local Server", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun allRuntimePermissionsGranted(): Boolean {
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val permissionsToRequest = ArrayList<String>()
        for (permission in REQUIRED_RUNTIME_PERMISSIONS) {
            permission.let {
                if (!isPermissionGranted(this, it)) {
                    permissionsToRequest.add(permission)
                }
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnMenu -> mViewDataBinding?.drawerLayout?.openDrawer(GravityCompat.START)
                R.id.ll_register_face -> {
                    startActivityWithOneValue(ConstantUtils.TYPE_OPEN,ConstantUtils.TYPE_REGISTER_FACE,
                        ManagerMenuActivity::class.java)
                    mViewDataBinding?.drawerLayout?.closeDrawers()
                }
                R.id.ll_admin_console -> {
                    startActivityWithOneValue(ConstantUtils.TYPE_OPEN,ConstantUtils.TYPE_ADMIN_CONSOLE,
                        ManagerMenuActivity::class.java)
                    mViewDataBinding?.drawerLayout?.closeDrawers()
                }
                R.id.tv_en -> {
                    setNewLocale(ConstantUtils.Language.ENGLISH)
                }
                R.id.tv_vi -> {
                    setNewLocale(ConstantUtils.Language.VIETNAMESE)
                }
            }
        }
    }

    private fun setNewLocale(language: String) {
        viewModel.setNewLocale(this, language)

        val intent = intent.also { it.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }
        finish()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, android.R.anim.fade_in, android.R.anim.fade_out)
        } else {
            startActivity(intent, ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())
        }
    }

    override fun getReturnAvailableLockersSuccess() {
        startActivity(ReturnActivity::class.java)
    }
}