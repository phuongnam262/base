package com.lock.smartlocker.ui.manager_menu

import com.lock.smartlocker.data.repositories.UserFaceRepository
import com.lock.smartlocker.ui.base.BaseViewModel

class ManagerMenuViewModel(
    private val userLockerRepository: UserFaceRepository
) : BaseViewModel() {
    var managerMenuListener: ManagerMenuListener? = null


}