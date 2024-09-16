package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.db.AppDatabase
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import com.lock.smartlocker.data.network.SafeApiRequest
import com.lock.smartlocker.data.network.LockerAPI

class StartAppRepository (
    private val api: LockerAPI
) : SafeApiRequest() {

    suspend fun terminalLogin(): BaseResponse<TerminalLoginResponse> {
        return apiRequest { api.provideLockerAPIService().terminalLogin("4ddfa0a2a99428d4136e") }
    }

    suspend fun getSetting(): BaseResponse<GetSettingResponse> {
        return apiRequest { api.provideLockerAPIService().getSetting() }
    }

    suspend fun listCategory(): BaseResponse<GetListCategoryResponse> {
        return apiRequest { api.provideLockerAPIService().listCategory() }
    }
}