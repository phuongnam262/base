package com.lock.basesource.data.repositories

import com.lock.basesource.data.entities.request.VerifyOTPRequest
import com.lock.basesource.data.entities.responses.BaseResponse
import com.lock.basesource.data.entities.responses.ConsumerLoginResponse
import com.lock.basesource.data.network.AppAPI
import com.lock.basesource.data.network.SafeApiRequest

class AppRepository (
    private val api: AppAPI
) : SafeApiRequest() {

    suspend fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.provideAppAPIService().verifyOTP(verifyOTPRequest) }
    }
}