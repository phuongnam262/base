package com.lock.basesource.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lock.basesource.R
import com.lock.basesource.data.network.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

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
    var statusText = MutableLiveData<String>()
    var showStatusText = MutableLiveData<Boolean>()

    /**
     * Init viewModel
     */
    open fun initViewModel() {
    }

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

            Status.NO_RESPONSE.value.toString() -> mMessage.postValue(R.string.server_error)

            else -> mErrorAllWithStatusCode.postValue(status ?: "99999")

        }
    }
}