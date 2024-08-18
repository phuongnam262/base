package com.lock.smartlocker.data.network.services

import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.GetInformationStaffResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.ListReturnAvailableLockerResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import com.lock.smartlocker.data.models.ItemReturn
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
        @Body consumerLoginRequest: ConsumerLoginRequest
    ): Response<BaseResponse<ConsumerLoginResponse>>

    @POST("/api/admin/admin-login")
    suspend fun adminLogin(
        @Body adminLoginRequest: AminLoginRequest
    ): Response<BaseResponse<AdminLoginResponse>>

    @GET("/api/admin/get-information-staff")
    suspend fun getInformationStaff(): Response<BaseResponse<GetInformationStaffResponse>>

    @GET("/api/return/return-get-available-locker")
    suspend fun listReturnAvailableLockers(): Response<BaseResponse<ListReturnAvailableLockerResponse>>

    @POST("/api/return/get-item-return")
    suspend fun getItemReturn(
        @Body getItemReturnRequest: GetItemReturnRequest
    ): Response<BaseResponse<ItemReturn>>

    @POST("/api/return/return-item")
    suspend fun returnItem(
        @Body returnItemRequest: ReturnItemRequest
    ): Response<BaseResponse<Map<String, Any>>>

    companion object {
        operator fun invoke(retrofit: Retrofit): LockerServives {
            return retrofit.create(LockerServives::class.java)
        }
    }

}