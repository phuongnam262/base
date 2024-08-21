package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.GetInformationStaffResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class ManagerRepository (
    private val api: LockerAPI
) : SafeApiRequest() {

    suspend fun consumerLogin(consumerLoginRequest: ConsumerLoginRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.provideLockerAPIService().consumerLogin(consumerLoginRequest) }
    }

    suspend fun adminLogin(adminLoginRequest: AminLoginRequest): BaseResponse<AdminLoginResponse> {
        return apiRequest { api.provideLockerAPIService().adminLogin(adminLoginRequest) }
    }

    suspend fun getInformationStaff(): BaseResponse<GetInformationStaffResponse> {
        return apiRequest { api.provideLockerAPIService().getInformationStaff() }
    }

}