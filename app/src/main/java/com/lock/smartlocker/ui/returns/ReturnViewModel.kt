package com.lock.smartlocker.ui.returns

import com.lock.smartlocker.data.repositories.ReturnRepository
import com.lock.smartlocker.ui.base.BaseViewModel

class ReturnViewModel(
    private val returnRepository: ReturnRepository
) : BaseViewModel() {
    var returnListener: ReturnListener? = null

}
