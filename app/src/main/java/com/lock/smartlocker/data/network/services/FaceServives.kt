package com.lock.smartlocker.data.network.services

import com.lock.smartlocker.data.entities.AddGroupModel
import com.lock.smartlocker.data.entities.request.AddPersonRequest
import com.lock.smartlocker.data.entities.request.ImageBase64Request
import com.lock.smartlocker.data.entities.request.ImageSearchRequest
import com.lock.smartlocker.data.entities.responses.BaseFaceResponse
import com.lock.smartlocker.data.entities.responses.BaseResponse
import com.lock.smartlocker.data.entities.responses.DetectImageResponse
import com.lock.smartlocker.data.entities.responses.SearchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FaceServives {
    @POST("addGroup")
    suspend fun addGroup(
        @Body groupModel: AddGroupModel
    ): Response<BaseFaceResponse>

    @GET("status")
    suspend fun getStatus(): Response<BaseFaceResponse>

    @POST("detect")
    suspend fun detectImage(
        @Body img: ImageBase64Request,
    ): Response<DetectImageResponse>

    @POST("searchPerson")
    suspend fun searchPerson(
        @Body img: ImageSearchRequest,
    ): Response<SearchResponse>

    @POST("addPerson")
    suspend fun addPerson(
        @Body person: AddPersonRequest,
    ): Response<BaseFaceResponse>

    @DELETE("deletePerson")
    suspend fun deletePerson(
        @Query("personCode") personCode: String
    ): Response<BaseFaceResponse>

    @DELETE("deletePerson")
    suspend fun deleteAllPerson(
        @Query("groupId") groupId: String
    ): Response<BaseFaceResponse>

    companion object {
        operator fun invoke(retrofit: Retrofit): FaceServives {
            return retrofit.create(FaceServives::class.java)
        }
    }

}