package com.lock.smartlocker.ui.loan

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.lock.smartlocker.BR
import com.lock.smartlocker.R
import com.lock.smartlocker.databinding.ActivityLoanBinding
import com.lock.smartlocker.ui.base.BaseActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoanActivity : BaseActivity<ActivityLoanBinding, LoanViewModel>(),
    KodeinAware, OnClickListener {

    override val kodein by kodein()
    private val factory: LoanViewModelFactory by instance()
    override val layoutId: Int
        get() = R.layout.activity_loan
    override val bindingVariable: Int
        get() = BR.viewmodel
    override val viewModel: LoanViewModel
        get() = ViewModelProvider(this, factory)[LoanViewModel::class.java]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

            }
        }
    }
}