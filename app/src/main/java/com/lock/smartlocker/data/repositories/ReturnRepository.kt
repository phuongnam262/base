package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.entities.request.GetItemReturnRequest
import com.lock.smartlocker.data.entities.request.ReturnItemRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.ListReturnAvailableLockerResponse
import com.lock.smartlocker.data.models.ItemReturn
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class ReturnRepository (
    private val api: LockerAPI
) : SafeApiRequest() {
    suspend fun listReturnAvailableLockers(): BaseResponse<ListReturnAvailableLockerResponse> {
        return apiRequest { api.provideLockerAPIService().listReturnAvailableLockers() }
    }

    suspend fun getItemReturn(getItemReturnRequest: GetItemReturnRequest) : BaseResponse<ItemReturn> {
        return apiRequest { api.provideLockerAPIService().getItemReturn(getItemReturnRequest) }
    }

    suspend fun getItemTopup(getItemReturnRequest: GetItemReturnRequest) : BaseResponse<ItemReturn> {
        return apiRequest { api.provideLockerAPIService().getItemTopup(getItemReturnRequest) }
    }

    suspend fun returnItem(returnItemRequest: ReturnItemRequest) : BaseResponse<Map<String, Any>> {
        return apiRequest { api.provideLockerAPIService().returnItem(returnItemRequest) }
    }

    suspend fun topupItem(returnItemRequest: ReturnItemRequest) : BaseResponse<Map<String, Any>> {
        return apiRequest { api.provideLockerAPIService().topupItem(returnItemRequest) }
    }
}