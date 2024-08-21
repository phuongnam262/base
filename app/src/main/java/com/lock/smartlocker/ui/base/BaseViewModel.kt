package com.lock.smartlocker.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lock.smartlocker.R
import com.lock.smartlocker.data.network.Status
import com.lock.smartlocker.data.preference.PreferenceHelper
import com.lock.smartlocker.util.ConstantUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

abstract class BaseViewModel() : ViewModel() {
    val mLoading = MutableLiveData<Boolean>().apply { value = false }
    val mMessage = MutableLiveData<Int>()
    val mStatusText = MutableLiveData<Int>()

    // coroutines
    private var viewModelJob = Job()
    private val ioContext = viewModelJob + Dispatchers.IO // background context
    private val uiContext = viewModelJob + Dispatchers.Main // ui context
    protected val ioScope = CoroutineScope(ioContext)
    protected val uiScope = CoroutineScope(uiContext)

    // Data binding
    var terminalName = PreferenceHelper.getString(ConstantUtils.TERMINAL_NAME, "")
    var timerString = MutableLiveData<String>()
    var ownerText = PreferenceHelper.getString(ConstantUtils.OWNER_TEXT, "")
    var titlePage = MutableLiveData<String>()
    var statusText = MutableLiveData<String>()
    var numberCart = MutableLiveData<String>()
    var showButtonProcess = MutableLiveData<Boolean>(true)
    var showStatusText = MutableLiveData<Boolean>()
    var isErrorText = MutableLiveData<Boolean>()
    var timerHour = MutableLiveData<String>()
    var imageBackgroundUrl = PreferenceHelper.getString(
        ConstantUtils.BACKGROUND,
        "https://uatalamapisapp.smartlocker.vn/Images/home-background.png"
    )

    fun startTimer() {
        uiScope.launch {
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    val currentTime = Date()
                    val formattedTime = SimpleDateFormat(
                        "HH:mm dd/MM/yyyy",
                        Locale.getDefault()
                    ).format(currentTime)
                    val formattedHour = SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                    ).format(currentTime)
                    timerString.postValue(formattedTime)
                    timerHour.postValue(formattedHour)
                }
            }, 0, 1000)
        }
    }

    /**
     * Init viewModel
     */
    open fun initViewModel() {}

    /**
     * Clearing viewModelJob when this viewModel already destroyed
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Handling show message or snackbar
     */
    fun handleError(status: String?) {
        when (status) {
            Status.NETWORK_ERROR.value.toString() -> mMessage.postValue(R.string.error_network)
            Status.EXCEPTION.value.toString() -> mMessage.postValue(R.string.error_message)
            Status.UNAUTHORIZED.value.toString() -> mMessage.postValue(R.string.unauthorized)
            Status.NO_RESPONSE.value.toString() -> mMessage.postValue(R.string.server_error)

            ConstantUtils.LOGIN_EMAIL_NOT_EXISTED -> {
                mStatusText.postValue(R.string.login_error_1001)
                isErrorText.postValue(true)
            }

            ConstantUtils.LOGIN_WRONG_PASSWORD ->  {
                mStatusText.postValue(R.string.login_error_1002)
                isErrorText.postValue(true)
            }

            ConstantUtils.EMAIL_NOT_CORRECT_FORMAT -> {
                mStatusText.postValue(R.string.email_not_correct_format)
                isErrorText.postValue(true)
            }

            ConstantUtils.ADMIN_WRONG_USERNAME_PASS -> {
                mStatusText.postValue(R.string.error_username_password)
                isErrorText.postValue(true)
            }

            ConstantUtils.ADMIN_ACCOUNT_LOCKED -> {
                mStatusText.postValue(R.string.error_account_locked)
                isErrorText.postValue(true)
            }

            ConstantUtils.DOOR_HAS_NOT_BEEN_CLOSE -> {
                mStatusText.postValue(R.string.door_has_not_been_closed)
                isErrorText.postValue(true)
            }

            ConstantUtils.SERIAL_NUMBER_INVALID, ConstantUtils.SERIAL_NUMBER_INVALID_1 -> {
                mStatusText.postValue(R.string.error_invalid_serial)
                isErrorText.postValue(true)
            }

            ConstantUtils.ERROR_LOGIC -> {
                mMessage.postValue(R.string.error_logic)
            }

            else -> mMessage.postValue(R.string.error_all)

        }
    }
}