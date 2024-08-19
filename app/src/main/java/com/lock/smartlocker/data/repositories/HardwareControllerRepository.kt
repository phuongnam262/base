package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.CheckMassLockerResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class HardwareControllerRepository(
    private val api: LockerAPI
) : SafeApiRequest() {
    suspend fun checkMassLocker(hardwareControllerRequest: HardwareControllerRequest): BaseResponse<CheckMassLockerResponse> {
        return apiRequest { api.provideHWCAPIService().checkMassLocker(hardwareControllerRequest) }
    }

    suspend fun openMassLocker(hardwareControllerRequest: HardwareControllerRequest): BaseResponse<CheckMassLockerResponse> {
        return apiRequest { api.provideHWCAPIService().openMassLocker(hardwareControllerRequest) }
    }
}
