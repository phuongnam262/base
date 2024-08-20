package com.lock.smartlocker.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.lock.smartlocker.R
import com.lock.smartlocker.util.CommonUtils

/**
 * process base logic for all activities
 */
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : BaseAppCompatActivity() {

    protected var mViewDataBinding: T? = null
        private set
    protected var mViewModel: V? = null
        private set

    /**
     * Overriding for set LayoutId
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int?

    /**
     * Overriding for set binding variable
     * @return variable id
     */
    abstract val bindingVariable: Int

    /**
     * Overriding for set view model
     * @return view model instance
     */
    abstract val viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setupObservers()
    }

    /**
     * Processing binding data
     */
    private fun performDataBinding() {
        mViewDataBinding = layoutId?.let { DataBindingUtil.setContentView(this, it) }
        //
        mViewModel = if (mViewModel == null) viewModel else mViewModel
        mViewDataBinding?.setVariable(bindingVariable, mViewModel)
        mViewDataBinding?.lifecycleOwner = this
        mViewDataBinding?.executePendingBindings()
    }

    /**
     * Handling show loading icon, message, snackbar
     */
    private fun setupObservers() {
        mViewModel?.mLoading?.observe(this, Observer {
            if (it != null && it) showLoading()
            else hideLoading()
        })
        mViewModel?.mMessage?.observe(this, Observer {
            it?.let { CommonUtils.showErrorDialog(this, "", getString(it)) }
        })
        mViewModel?.mStatusText?.observe(this, Observer {
            it?.let { mViewModel?.statusText?.postValue(getString(it)) }
            mViewModel?.showStatusText?.postValue(true)
        })
    }

    /**
     * Handler action move delay
     */
    fun runDelay(delay: Long, process: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            process()
        }, delay)
    }

    fun startActivity( activity: Class<*>) {
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    fun startActivityWithOneValue(strKey: String, strValue: String, activity: Class<*>) {
        val intent = Intent(this, activity)
        intent.putExtra(strKey, strValue)
        startActivity(intent)
    }

    fun startActivityWithTwoValue(
        strKey1: String,
        strValue1: String,
        strKey2: String,
        strValue2: String,
        activity: Class<*>
    ) {
        val intent = Intent(this, activity)
        intent.putExtra(strKey1, strValue1)
        intent.putExtra(strKey2, strValue2)
        startActivity(intent)
    }

    fun startActivityWithThreeValue(
        strKey1: String,
        strValue1: String,
        strKey2: String,
        strValue2: String,
        strKey3: String,
        strValue3: String,
        activity: Class<*>
    ) {
        val intent = Intent(this, activity)
        intent.putExtra(strKey1, strValue1)
        intent.putExtra(strKey2, strValue2)
        intent.putExtra(strKey3, strValue3)
        startActivity(intent)
    }

    // In ManagerMenuActivity.kt
    fun showFragment(fragment: Fragment, navGraphId: Int, addToBackStack: Boolean = false) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_main) as NavHostFragment
        val navController = navHostFragment.navController
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(navGraphId)
        navController.graph = graph

        if (addToBackStack) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit()
        }
    }
}