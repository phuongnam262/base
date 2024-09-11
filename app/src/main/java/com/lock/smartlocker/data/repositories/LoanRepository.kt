package com.lock.smartlocker.data.repositories

import com.lock.smartlocker.data.entities.request.CreateInventoryTransactionRequest
import com.lock.smartlocker.data.entities.request.GetAvailableItemRequest
import com.lock.smartlocker.data.entities.request.UpdateInventoryTransactionRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.CreateInventoryResponse
import com.lock.smartlocker.data.entities.responses.GetAvailableItemResponse
import com.lock.smartlocker.data.entities.responses.GetConsumableAvailableItemResponse
import com.lock.smartlocker.data.network.LockerAPI
import com.lock.smartlocker.data.network.SafeApiRequest

class LoanRepository(
    private val api: LockerAPI
) : SafeApiRequest() {
    suspend fun getAvailableItem(getAvailableItemRequest: GetAvailableItemRequest): BaseResponse<GetAvailableItemResponse> {
        return apiRequest { api.provideLockerAPIService().getAvailableItem(getAvailableItemRequest) }
    }

    suspend fun getConsumableAvailableItem(): BaseResponse<GetConsumableAvailableItemResponse> {
        return apiRequest { api.provideLockerAPIService().getConsumableAvailableItem() }
    }

    suspend fun createInventoryTransaction(createInventoryTransactionRequest: CreateInventoryTransactionRequest): BaseResponse<CreateInventoryResponse> {
        return apiRequest { api.provideLockerAPIService().createInventoryTransaction(createInventoryTransactionRequest) }
    }

    suspend fun updateInventoryTransaction(updateInventoryTransactionRequest: UpdateInventoryTransactionRequest): BaseResponse<Any> {
        return apiRequest { api.provideLockerAPIService().updateInventoryTransaction(updateInventoryTransactionRequest) }
    }
}