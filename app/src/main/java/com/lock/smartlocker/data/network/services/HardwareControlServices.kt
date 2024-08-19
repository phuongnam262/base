package com.lock.smartlocker.data.network.services

import com.lock.smartlocker.data.entities.request.HardwareControllerRequest
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.CheckMassLockerResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface HardwareControlServices {

    @POST("/api/lockers/check-mass-locker")
    suspend fun checkMassLocker(
        @Body hardwareControllerRequest: HardwareControllerRequest
    ): Response<BaseResponse<CheckMassLockerResponse>>

    @POST("/api/lockers/open-mass-locker")
    suspend fun openMassLocker(
        @Body hardwareControllerRequest: HardwareControllerRequest
    ): Response<BaseResponse<CheckMassLockerResponse>>

    companion object {
        operator fun invoke(retrofit: Retrofit): HardwareControlServices {
            return retrofit.create(HardwareControlServices::class.java)
        }
    }

}