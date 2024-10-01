package com.lock.smartlocker.data.entities.responses

import com.lock.smartlocker.data.models.Staff
import java.io.Serializable

class AdminLoginResponse(
    val staff: Staff
): Serializable

