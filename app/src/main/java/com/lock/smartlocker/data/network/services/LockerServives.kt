package com.lock.smartlocker.data.network.services

import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LockerServives {

    @POST("/api/auth/terminal-login")
    suspend fun terminalLogin(
        @Header("terminal_id") terminalId: String
    ): Response<BaseResponse<TerminalLoginResponse>>

    @GET("/api/terminal/get-settings")
    suspend fun getSetting(): Response<BaseResponse<GetSettingResponse>>

    @GET("/api/terminal/list-category")
    suspend fun listCategory(): Response<BaseResponse<GetListCategoryResponse>>

    @POST("/api/consumer/consumer-login")
    suspend fun consumerLogin(
        @Body consumerLoginResponse: ConsumerLoginRequest
    ): Response<BaseResponse<ConsumerLoginResponse>>

    companion object {
        operator fun invoke(retrofit: Retrofit): LockerServives {
            return retrofit.create(LockerServives::class.java)
        }
    }

}