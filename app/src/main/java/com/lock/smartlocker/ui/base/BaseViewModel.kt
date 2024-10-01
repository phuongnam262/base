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
    val mOtherError = MutableLiveData<String>()
    val mErrorAllWithStatusCode = MutableLiveData<String>()

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
    var showButtonProcess = MutableLiveData<Boolean>(true)
    var enableButtonProcess = MutableLiveData<Boolean>(false)
    var showButtonUsingMail = MutableLiveData<Boolean>(true)
    var showStatusText = MutableLiveData<Boolean>()
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
            ConstantUtils.ERROR_NO_AVAILABLE_ITEM -> mMessage.postValue(R.string.error_no_available_item)

            ConstantUtils.LOGIN_EMAIL_NOT_EXISTED -> mStatusText.postValue(R.string.login_error_1001)

            ConstantUtils.LOGIN_WRONG_PASSWORD -> mStatusText.postValue(R.string.login_error_1002)

            ConstantUtils.EMAIL_NOT_CORRECT_FORMAT -> mStatusText.postValue(R.string.email_not_correct_format)

            ConstantUtils.ADMIN_WRONG_USERNAME_PASS -> mStatusText.postValue(R.string.error_username_password)

            ConstantUtils.ADMIN_ACCOUNT_LOCKED -> mStatusText.postValue(R.string.error_account_locked)

            ConstantUtils.DOOR_HAS_NOT_BEEN_CLOSE -> mStatusText.postValue(R.string.door_has_not_been_closed)

            ConstantUtils.SERIAL_NUMBER_INVALID, ConstantUtils.SERIAL_NUMBER_INVALID_1 -> mStatusText.postValue(R.string.error_invalid_serial)

            ConstantUtils.ERROR_LOGIC -> mMessage.postValue(R.string.error_logic)

            ConstantUtils.INVALID_OTP -> mStatusText.postValue(R.string.error_invalid_otp)

            ConstantUtils.CREATE_TRANSACTION_FAILED -> mStatusText.postValue(R.string.error_create_transaction_failed)

            ConstantUtils.ERROR_UNABLE_LOCKER -> mStatusText.postValue(R.string.error_unable_locker)

            ConstantUtils.PASSWORD_POLICY_UPDATE -> mStatusText.postValue(R.string.error_password_policy_update)

            ConstantUtils.PASSWORD_EXPIRED -> mStatusText.postValue(R.string.error_password_expired)

            ConstantUtils.ADMIN_NO_PERMISSION -> mStatusText.postValue(R.string.error_no_permission_locker)

            ConstantUtils.ERROR_NO_RETRIEVE_ITEM -> mStatusText.postValue(R.string.error_no_item_available)

            ConstantUtils.ERROR_RETRIEVE_ITEM_FAIL -> mStatusText.postValue(R.string.error_retrieve_item)

            ConstantUtils.ERROR_NO_RETRIEVE_FAULTY -> mStatusText.postValue(R.string.error_no_faulty)

            ConstantUtils.ERROR_CARD_NUMBER -> mStatusText.postValue(R.string.error_wrong_card_number)

            ConstantUtils.ERROR_SERIAL_EXISTED -> mStatusText.postValue(R.string.error_serial_existed)

            ConstantUtils.ERROR_NO_LOCKER_FOUND -> mStatusText.postValue(R.string.error_no_locker_found)

            else -> mErrorAllWithStatusCode.postValue(status ?: "99999")

        }
    }
}