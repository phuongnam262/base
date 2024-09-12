package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.entities.request.AminLoginRequest
import com.lock.smartlocker.data.entities.request.CheckCardRequest
import com.lock.smartlocker.data.entities.request.ConsumerLoginRequest
import com.lock.smartlocker.data.entities.request.DisableLockerRequest
import com.lock.smartlocker.data.entities.request.RetrieveItemRequest
import com.lock.smartlocker.data.entities.request.VerifyOTPRequest
import com.lock.smartlocker.data.entities.responses.AdminLoginResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.CheckCardResponse
import com.lock.smartlocker.data.entities.responses.ConsumerLoginResponse
import com.lock.smartlocker.data.entities.responses.DisableLockerResponse
import com.lock.smartlocker.data.entities.responses.GetAllItemRetrieveResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableAvailableItemResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableInLockerResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableResponse
import com.lock.smartlocker.data.entities.responses.GetInformationStaffResponse
import com.lock.smartlocker.data.entities.responses.GetSettingResponse
import com.lock.smartlocker.data.entities.responses.RetrieveItemResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class ManagerRepository (
    private val api: LockerAPI
) : SafeApiRequest() {

    suspend fun consumerLogin(consumerLoginRequest: ConsumerLoginRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.provideLockerAPIService().consumerLogin(consumerLoginRequest) }
    }

    suspend fun checkCardNumber(checkCardRequest: CheckCardRequest): BaseResponse<CheckCardResponse> {
        return apiRequest { api.provideLockerAPIService().checkCardNumber(checkCardRequest) }
    }

    suspend fun adminLogin(adminLoginRequest: AminLoginRequest): BaseResponse<AdminLoginResponse> {
        return apiRequest { api.provideLockerAPIService().adminLogin(adminLoginRequest) }
    }

    suspend fun getInformationStaff(): BaseResponse<GetInformationStaffResponse> {
        return apiRequest { api.provideLockerAPIService().getInformationStaff() }
    }

    suspend fun getSetting(): BaseResponse<GetSettingResponse> {
        return apiRequest { api.provideLockerAPIService().getSetting() }
    }

    suspend fun disableLocker(disableLockerRequest: DisableLockerRequest): BaseResponse<DisableLockerResponse> {
        return apiRequest { api.provideLockerAPIService().disableLocker(disableLockerRequest) }
    }

    suspend fun getAllItemRetrieve(): BaseResponse<GetAllItemRetrieveResponse> {
        return apiRequest { api.provideLockerAPIService().getAllItemRetrieve() }
    }

    suspend fun getItemFaulty(): BaseResponse<GetAllItemRetrieveResponse> {
        return apiRequest { api.provideLockerAPIService().getItemFaulty() }
    }

    suspend fun retrieveItem(retrieveItemRequest: RetrieveItemRequest): BaseResponse<RetrieveItemResponse> {
        return apiRequest { api.provideLockerAPIService().retrieveItem(retrieveItemRequest) }
    }

    suspend fun verifyOTP(verifyOTPRequest: VerifyOTPRequest): BaseResponse<ConsumerLoginResponse> {
        return apiRequest { api.provideLockerAPIService().verifyOTP(verifyOTPRequest) }
    }

    suspend fun resendOTP(consumerLoginRequest: ConsumerLoginRequest): BaseResponse<Map<String, Any>> {
        return apiRequest { api.provideLockerAPIService().resendOTP(consumerLoginRequest) }
    }

    suspend fun getConsumable(): BaseResponse<GetConsumableResponse> {
        return apiRequest { api.provideLockerAPIService().getConsumable() }
    }

    suspend fun getConsumableInLocker(getConsumableInLockerResponse: GetConsumableInLockerResponse): BaseResponse<GetConsumableInLockerResponse> {
        return apiRequest { api.provideLockerAPIService().getConsumableInLocker(getConsumableInLockerResponse) }
    }
}