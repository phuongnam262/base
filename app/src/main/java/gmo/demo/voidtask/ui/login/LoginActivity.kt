package gmo.demo.voidtask.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import gmo.demo.voidtask.BR
import gmo.demo.voidtask.R
import gmo.demo.voidtask.data.repositories.UserRepository
import gmo.demo.voidtask.databinding.ActivityLoginBinding
import gmo.demo.voidtask.ui.base.BaseActivity
import gmo.demo.voidtask.ui.home.HomeActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    private val userRepository = UserRepository()
    private val factory = LoginViewModelFactory(userRepository)
    override val layoutId: Int get() = R.layout.activity_login
    override val bindingVariable: Int get() = BR.viewmodel
    override val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding?.btnLogin?.setOnClickListener {
            val email = mViewDataBinding?.edtEmail?.text.toString()
            val password = mViewDataBinding?.edtPassword?.text.toString()
            viewModel.login(email, password)
        }
        viewModel.loginResult.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        viewModel.errorMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }
}