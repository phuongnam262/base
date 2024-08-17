package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import com.lock.smartlocker.data.network.SafeApiRequest
import com.lock.smartlocker.data.network.LockerAPI

class StartAppRepository (
    private val api: LockerAPI,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun terminalLogin(): BaseResponse<TerminalLoginResponse> {
        return apiRequest { api.provideLockerAPIService().terminalLogin("4430a9956b242edd6721") }
    }

    suspend fun getSetting(): BaseResponse<GetSettingResponse> {
        return apiRequest { api.provideLockerAPIService().getSetting() }
    }

    suspend fun listCategory(): BaseResponse<GetListCategoryResponse> {
        return apiRequest { api.provideLockerAPIService().listCategory() }
    }
}