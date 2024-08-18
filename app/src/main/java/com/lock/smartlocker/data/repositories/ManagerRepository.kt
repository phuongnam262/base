package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import com.lock.smartlocker.data.network.SafeApiRequest
import com.lock.smartlocker.data.network.LockerAPI

class ManagerRepository (
    private val api: LockerAPI,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun consumerLogin(consumerLoginRequest: ConsumerLoginRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.provideLockerAPIService().consumerLogin(consumerLoginRequest) }
    }

    suspend fun adminLogin(adminLoginRequest: AminLoginRequest): BaseResponse<AdminLoginResponse> {
        return apiRequest { api.provideLockerAPIService().adminLogin(adminLoginRequest) }
    }

}