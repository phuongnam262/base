package gmo.demo.voidtask.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gmo.demo.voidtask.data.repositories.UserRepository

class LoginViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}