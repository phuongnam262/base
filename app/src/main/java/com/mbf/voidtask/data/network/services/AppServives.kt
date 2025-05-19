package com.lock.basesource.data.network.services

import com.lock.basesource.data.entities.request.VerifyOTPRequest
import com.lock.basesource.data.entities.responses.BaseResponse
import com.lock.basesource.data.entities.responses.ConsumerLoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface AppServives {

    @POST("/api/verify-otp")
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Response<BaseResponse<ConsumerLoginResponse>>

    companion object {
        operator fun invoke(retrofit: Retrofit): AppServives {
            return retrofit.create(AppServives::class.java)
        }
    }

}