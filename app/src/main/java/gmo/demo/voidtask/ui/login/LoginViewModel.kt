package gmo.demo.voidtask.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import gmo.demo.voidtask.data.repositories.UserRepository
import gmo.demo.voidtask.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val loginResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                mLoading.value = true
                val users = userRepository.getUsers()
                val user = users.find { it.username == email && it.password == password }
                if (user != null) {
                    loginResult.value = true
                } else {
                    errorMessage.value = "Login failed"
                }
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            } finally {
                mLoading.value = false
            }
        }
    }
}