package com.lock.smartlocker.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.lock.smartlocker.R
import com.lock.smartlocker.util.CommonUtils
import com.lock.smartlocker.util.Coroutines
import com.lock.smartlocker.util.view.custom.CustomLoading

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    private var mActivity: BaseAppCompatActivity? = null
    protected var mViewDataBinding: T? = null
        private set
    protected var mViewModel: V? = null
        private set
    private var mDialog: Dialog? = null

    /**
     * Overriding for set layoutId variable
     * @return layout resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseAppCompatActivity) {
            mActivity = context
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewModel.startTimer()
        return mViewDataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding?.setVariable(bindingVariable, mViewModel)
        mViewDataBinding?.lifecycleOwner = viewLifecycleOwner
        setupObservers()
    }

    protected fun navigateTo(id: Int, bundle: Bundle?) {
        try {
            Coroutines.main {
                Navigation.findNavController(mViewDataBinding?.root!!)
                    .navigate(id, bundle)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Handling show loading icon, message, snackbar for fragment
     */
    private fun setupObservers() {
        activity?.let {activity ->
            mViewModel?.mLoading?.observe(activity, Observer {
                if (it != null && it) showLoading()
                else hideLoading()
            })
            mViewModel?.mMessage?.observe(activity, Observer {
                it?.let { CommonUtils.showErrorDialog(activity, "", getString(it)) }
            })
            mViewModel?.mStatusText?.observe(activity, Observer {
                it?.let { mViewModel?.statusText?.postValue(getString(it)) }
                mViewModel?.showStatusText?.postValue(true)
            })
            mViewModel?.mOtherError?.observe(activity, Observer {
                it?.let { CommonUtils.showErrorDialog(activity, "", it) }
            })
        }
    }

    /**
     * Create and show progress
     */
    @SuppressLint("InflateParams")
    open fun showLoading() {
        mDialog ?: let {
            mDialog = Dialog(requireActivity())
            val inflater =
                LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_loading, null)
            mDialog?.setContentView(inflater)
            mDialog?.setCancelable(false)
            mDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            mDialog?.setOnShowListener {
                mDialog?.window?.decorView?.findViewById<CustomLoading>(R.id.custom_loading)
                    ?.startAnimation()
            }
        }

        if (mDialog?.isShowing != true) {
            mDialog?.show()
        }
    }

    /**
     * hide progress
     */
    open fun hideLoading() {
        if (mDialog?.isShowing == true) {
            mDialog?.dismiss()
            mDialog?.window?.decorView?.findViewById<CustomLoading>(R.id.custom_loading)
                ?.stopAnimation()
        }
    }

    /**
     * Execute backPressed
     */
    private fun doBackPressed() {
        mActivity?.onBackPressed()
    }

    /**
     * Base on activity
     */
    protected fun setFragment(fragment: Fragment) {
        mActivity?.setFragment(fragment)
    }

    /**
     * Taking rootView
     */
    private fun getRootView(): View? = mActivity?.getRootView()

    /**
     * Base on activity
     */
    protected fun hideKeyboard() {
        mActivity?.hideKeyboard()
    }

    /**
     * Base on activity
     */
    protected fun showKeyboard(view: View) {
        mActivity?.showKeyBoard(view)
    }
}