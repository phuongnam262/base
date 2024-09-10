package com.lock.smartlocker.ui.admin_login

import com.lock.smartlocker.data.entities.responses.AdminLoginResponse

interface AdminLoginListener {
    fun adminLoginSuccess( adminLoginResponse: AdminLoginResponse)
    fun adminLoginFail()
}