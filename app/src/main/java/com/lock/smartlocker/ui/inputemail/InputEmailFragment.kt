package com.lock.smartlocker.ui.inputemail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.FragmentInputEmailBinding
import com.lock.smartlocker.ui.base.BaseFragment
import com.lock.smartlocker.util.ConstantUtils
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class InputEmailFragment : BaseFragment<FragmentInputEmailBinding, InputEmailViewModel>(), KodeinAware,
    View.OnClickListener, InputEmailListener {

    override val kodein by kodein()
    private val factory: InputEmailViewModelFactory by instance()
    override val layoutId: Int = R.layout.fragment_input_email
    override val bindingVariable: Int
        get() = BR.viewmodel

    override val viewModel: InputEmailViewModel
        get() = ViewModelProvider(requireActivity(), factory)[InputEmailViewModel::class.java]

    companion object {
        const val EMAIL_REGISTER = "email_register"
    }

    private val items = listOf("@gmail.com", "@yahoo.com", "@hotmail.com", "@edu")
    private var isClicked = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.inputEmailListener = this
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        isClicked = false
    }

    private fun initView(){
        viewModel.titlePage.postValue(getString(R.string.auth_required))

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mViewDataBinding?.spiEmail?.adapter = adapter
        viewModel.subEmail.value = items[0]

        mViewDataBinding?.spiEmail?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val subEmail = parent.getItemAtPosition(position) as String
                viewModel.subEmail.value = subEmail
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
        mViewDataBinding?.bottomMenu?.rlHome?.setOnClickListener(this)
        mViewDataBinding?.bottomMenu?.btnProcess?.setOnClickListener(this)
        mViewDataBinding?.headerBar?.ivBack?.setOnClickListener(this)
        viewModel.enableButtonProcess.postValue(true)
    }

    private fun initData(){
        if (arguments?.getString(ConstantUtils.TYPE_OPEN) != null) {
            viewModel.typeOpen = arguments?.getString(ConstantUtils.TYPE_OPEN)
        }
    }

    override fun onClick(v: View?) {
        if (checkDebouncedClick()) {
            Log.d("InputOTPFragment", System.currentTimeMillis().toString())
            when (v?.id) {
                R.id.rl_home -> activity?.finish()
                R.id.iv_back -> activity?.onBackPressedDispatcher?.onBackPressed()
                R.id.btn_process -> {
                    if (isClicked.not()) {
                        isClicked = true
                        viewModel.consumerLogin()
                    }
                }
            }
        }
    }

    override fun consumerLoginSuccess(email: String?) {
        viewModel.email.postValue("")
        //viewModel.subEmail.value = items[0]
       // mViewDataBinding?.spiEmail?.setSelection(0)
        if (viewModel.typeOpen != null) {
            val bundle = Bundle().apply {
                putString(
                    ConstantUtils.TYPE_OPEN,
                    arguments?.getString(ConstantUtils.TYPE_OPEN)
                )
            }
            navigateTo(R.id.action_inputEmailFragment2_to_categoryFragment, bundle)
        }else{
            val bundle = Bundle().apply {
                putString(EMAIL_REGISTER, email)
            }
            navigateTo(R.id.action_inputEmailFragment_to_registerFaceFragment, bundle)
        }
    }

    override fun consumerLoginFail(email: String?, status: String?) {
        if (status == ConstantUtils.REQUIRE_OTP){
            if (viewModel.typeOpen != null) {
                val bundle = Bundle().apply {
                    putString(ConstantUtils.TYPE_OPEN, viewModel.typeOpen)
                    putString(EMAIL_REGISTER, email)
                }
                navigateTo(R.id.action_inputEmailFragment2_to_inputOTPFragment2, bundle)
            }else{
                val bundle = Bundle().apply {
                    putString(EMAIL_REGISTER, email)
                }
                navigateTo(R.id.action_navigation_input_email_to_inputOTPFragment, bundle)
            }
        }else isClicked = false
    }
}