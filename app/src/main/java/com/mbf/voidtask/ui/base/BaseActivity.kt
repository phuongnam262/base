package com.lock.basesource.ui.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.lock.basesource.util.CommonUtils

/**
 * process base logic for all activities
 */
abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : BaseAppCompatActivity(){

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

    private var isObserverSet = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
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
        if (isObserverSet.not()) {
            mViewModel?.mLoading?.observe(this) {
                if (it != null && it) showLoading()
                else hideLoading()
            }
            mViewModel?.mMessage?.observe(this) {
                it?.let { CommonUtils.showErrorDialog(this, "", getString(it)) }
            }
            mViewModel?.mStatusText?.observe(this) {
                it?.let { mViewModel?.statusText?.postValue(getString(it)) }
                mViewModel?.showStatusText?.postValue(true)
            }
            isObserverSet = true
        }
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

    private var lastClickTime = 0L
    fun checkDebouncedClick() : Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= 600L) {
            lastClickTime = currentTime
            return true
        }else return false
    }

    /**
     * Delay to run process
     */
    fun runTimeDelay(timeMoveDelay: Long, process: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            process()
        }, timeMoveDelay)
    }
}