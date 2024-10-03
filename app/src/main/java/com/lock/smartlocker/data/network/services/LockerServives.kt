package com.lock.smartlocker.data.network.services

import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.request.CheckCardRequest
import com.lock.smartlocker.data.entities.request.ConfirmConsumableCollectRequest
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.request.CreateInventoryTransactionRequest
import com.lock.smartlocker.data.entities.request.CreateTransactionRequest
import com.lock.smartlocker.data.entities.request.DisableLockerRequest
import com.lock.smartlocker.data.entities.request.EndUserLoginRequest
import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.entities.request.GetConsumableInLockerRequest
import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.entities.request.ItemRequest
import com.lock.smartlocker.data.entities.request.ReportConsumableRequest
import com.lock.smartlocker.data.entities.request.ReportConsumableTransactionRequest
import com.lock.smartlocker.data.entities.request.RetrieveItemRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.entities.request.TopupConsumableRequest
import com.lock.smartlocker.data.entities.request.UpdateInventoryTransactionRequest
import com.lock.smartlocker.data.entities.request.VerifyOTPRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.CheckCardResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.CreateInventoryResponse
import com.lock.smartlocker.data.entities.responses.CreateTransactionResponse
import com.lock.smartlocker.data.entities.responses.DisableLockerResponse
import com.lock.smartlocker.data.entities.responses.GetAllItemRetrieveResponse
import com.lock.smartlocker.data.entities.responses.GetAvailableItemResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableAvailableItemResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableInLockerResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableResponse
import com.lock.smartlocker.data.entities.responses.GetInformationStaffResponse
import com.lock.smartlocker.data.entities.responses.GetListCategoryResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.ListReturnAvailableLockerResponse
import com.lock.smartlocker.data.entities.responses.RetrieveItemResponse
import com.lock.smartlocker.data.entities.responses.TerminalLoginResponse
import com.lock.smartlocker.data.models.ItemReturn
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

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

    @POST("/api/end-user/login")
    suspend fun endUserLogin(
        @Body endUserLoginRequest: EndUserLoginRequest
    ): Response<BaseResponse<CheckCardResponse>>

    @POST("/api/consumer/verify-otp")
    suspend fun verifyOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Response<BaseResponse<ConsumerLoginResponse>>

    @POST("/api/consumer/verify-consumer-login")
    suspend fun resendOTP(
        @Body consumerLoginRequest: ConsumerLoginRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/admin/resend-otp")
    suspend fun resendAdminOTP(
        @Body consumerLoginRequest: ConsumerLoginRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/end-user/check-card-number")
    suspend fun checkCardNumber(
        @Body checkCardRequest: CheckCardRequest
    ): Response<BaseResponse<CheckCardResponse>>

    @POST("/api/admin/admin-login")
    suspend fun adminLogin(
        @Body adminLoginRequest: AminLoginRequest
    ): Response<BaseResponse<AdminLoginResponse>>

    @POST("/api/admin/verify-otp ")
    suspend fun verifyAdminOTP(
        @Body verifyOTPRequest: VerifyOTPRequest
    ): Response<BaseResponse<AdminLoginResponse>>

    @GET("/api/admin/get-information-staff")
    suspend fun getInformationStaff(): Response<BaseResponse<GetInformationStaffResponse>>

    @GET("/api/return/return-get-available-locker")
    suspend fun listReturnAvailableLockers(): Response<BaseResponse<ListReturnAvailableLockerResponse>>

    @POST("/api/return/get-item-return")
    suspend fun getItemReturn(
        @Body getItemReturnRequest: GetItemReturnRequest
    ): Response<BaseResponse<ItemReturn>>

    @POST("/api/item/get-item-topup")
    suspend fun getItemTopup(
        @Body getItemReturnRequest: GetItemReturnRequest
    ): Response<BaseResponse<ItemReturn>>

    @POST("/api/return/return-item")
    suspend fun returnItem(
        @Body returnItemRequest: ReturnItemRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @GET("/api/admin/get-consumables-in-terminal")
    suspend fun getConsumable(): Response<BaseResponse<GetConsumableResponse>>

    @POST("/api/admin/get-consumables-in-locker")
    suspend fun getConsumableInLocker(
        @Body getConsumableInLockerRequest: GetConsumableInLockerRequest
    ): Response<BaseResponse<GetConsumableInLockerResponse>>

    @POST("/api/item/create-item")
    suspend fun createItem(
        @Body itemRequest: ItemRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/item/update-item")
    suspend fun updateItem(
        @Body itemRequest: ItemRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/item/topup-item")
    suspend fun topupItem(
        @Body returnItemRequest: ReturnItemRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/admin/topup-consusmable")
    suspend fun topupConsumable(
        @Body topupConsumableRequest: TopupConsumableRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/inventory-transaction/get-available-item")
    suspend fun getAvailableItem(
        @Body getAvailableItemRequest: GetAvailableItemRequest
    ): Response<BaseResponse<GetAvailableItemResponse>>

    @GET("/api/consumable-transaction/get-available-item")
    suspend fun getConsumableAvailableItem(): Response<BaseResponse<GetConsumableAvailableItemResponse>>

    @POST("/api/inventory-transaction/create-inventory-transaction")
    suspend fun createInventoryTransaction(
        @Body createInventoryTransactionRequest: CreateInventoryTransactionRequest
    ): Response<BaseResponse<CreateInventoryResponse>>

    @POST("/api/inventory-transaction/update-inventory-transaction")
    suspend fun updateInventoryTransaction(
        @Body updateInventoryTransactionRequest: UpdateInventoryTransactionRequest
    ): Response<BaseResponse<Any>>

    @POST("/api/consumable-transaction/create-consusmable-transaction")
    suspend fun createConsumableTransaction(
        @Body createTransactionRequest: CreateTransactionRequest
    ): Response<BaseResponse<CreateTransactionResponse>>

    @POST("/api/consumable-transaction/confirm-collect-consusmable")
    suspend fun confirmCollectConsumable(
        @Body confirmConsumableCollectRequest: ConfirmConsumableCollectRequest
    ): Response<BaseResponse<Any>>

    @POST("/api/locker/disable-locker")
    suspend fun disableLocker(
        @Body disableLockerRequest: DisableLockerRequest
    ): Response<BaseResponse<DisableLockerResponse>>

    @GET("/api/admin/get-all-item-retrieve")
    suspend fun getAllItemRetrieve(
    ): Response<BaseResponse<GetAllItemRetrieveResponse>>

    @GET("/api/admin/get-item-faulty")
    suspend fun getItemFaulty(
    ): Response<BaseResponse<GetAllItemRetrieveResponse>>

    @POST("/api/admin/retrieve-item")
    suspend fun retrieveItem(
        @Body retrieveItemRequest: RetrieveItemRequest
    ): Response<BaseResponse<RetrieveItemResponse>>

    @POST("/api/admin/report-consusmable")
    suspend fun reportConsumable(
        @Body reportConsumableRequest: ReportConsumableRequest
    ): Response<BaseResponse<Map<String, Any>>>

    @POST("/api/consumable-transaction/report-consusmable")
    suspend fun reportConsumableTransaction(
        @Body reportConsumableTransactionRequest: ReportConsumableTransactionRequest
    ): Response<BaseResponse<Map<String, Any>>>

    companion object {
        operator fun invoke(retrofit: Retrofit): LockerServives {
            return retrofit.create(LockerServives::class.java)
        }
    }

}