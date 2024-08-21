package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.GetAvailableItemResponse
import com.lock.smartlocker.data.entities.responses.ListReturnAvailableLockerResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class LoanRepository(
    private val api: LockerAPI
) : SafeApiRequest() {
    suspend fun getAvailableItem(getAvailableItemRequest: GetAvailableItemRequest): BaseResponse<GetAvailableItemResponse> {
        return apiRequest { api.provideLockerAPIService().getAvailableItem(getAvailableItemRequest) }
    }
}